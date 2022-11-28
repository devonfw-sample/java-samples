#!/bin/bash

#Display a small help page with details about the script and optional flags
if [[ $* == *--help* ]]
then
    echo "This installer will generate an example deployment containing a spring server secured behind a oauth2 proxy using keyclaok as identity provider."
    echo "To run this script successfully Rancher Desktop has to be installed."
    echo ""
    echo "If you run Kubernetes with a the containerd runtime add --containerd"
    echo ""
    echo "To skip the port check add --ignore-block"
    exit
fi


#This is to prevent the scrip from running when port 80 is blocked
#Please make sure that nothing is blocking port 80 beacause if the port is blocked
#by something the ingress controller will not listen on localhost but on an external cluster ip
#We need to listen on localhost beacuse the service localtest.me will resolve to 127.0.0.1 without any modification in host files
portcount=$(ss -lntu | grep ':80' -c)

if [[ $* != *--ignore-block* ]] && [ "$portcount" -gt 1 ]
then
    echo "Port 80 is currently blocked by another programm."
    echo "The setup will not work if the ingress controller is not listening on localhost:80."
    echo "To skip this test run with --ignore-block"
    exit
fi

#List all docker images and check if the spring server imgae already got created
if [[ $* == *--containerd* ]]
then
    dockerimageresult=$(nerdctl images --namespace k8s.io | grep "spring-server")
else
    dockerimageresult=$(docker images | grep "spring-server")
fi


#Docker images doesn't exist so create it from the dockerfile
if [ -z "$dockerimageresult" ]
then
    #Build server
    mvn -f ./ProxyTestServer/pom.xml clean install
    #Create image
    if [[ $* == *--containerd* ]]
    then
        nerdctl build -t spring-server --namespace k8s.io ProxyTestServer/
    else
        docker build -t spring-server ProxyTestServer/   
    fi
fi


#Add the required helm repos for a esay keycloak and ingress controller deployment
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx


#Install the nginx ingress controller from a helm chart to use ingress components inside the k8s cluster
#This helm chart can be customized with a values.yaml file
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-nginx --create-namespace


#Wait for ingress controller to be fully running befor creating an ingress for it
#This is needed to prevent an error causing the ingress deployment to fail
kubectl wait deployment -n ingress-nginx ingress-nginx-controller --for condition=Available=True --timeout=-1s

#Add an ingress to route all requests to the proxy
#The ingress is using the created nginx controller installed in the prior step
#To test the application localy we use the service localtest.me
#This services is a fully qualified domain name that gets resolved to 127.0.0.1 by public dns servers
kubectl apply -f config/ingress.yaml


#Move the exported realm file from keycloak into a configmap 
#to access it when importing the realm back into keycloak during the deployment
#Currently there is no way to  add users directly 
#TODO: Richard Linde Add test users with and without groups 
kubectl create configmap keycloak-realm --from-file=config/keycloak/realm.json


#Create keycloak from a helm chart
#Usernames and passwords for keycloak and it's database can be found in the keyCloakValues file
helm upgrade --install keycloak bitnami/keycloak -f config/keyCloakValues.yaml


#Deploy the spring service that is secured behind the proxy
kubectl apply -f config/springServer.yaml


#Add the keyclak proxy to route all requests to
#The proxy will check the request for the nessesery permissions
#If the request is valid the proxy will redirect the request to the upstream(spring server)
#If not the browser of the user will redirect him to the oauth screen to login
kubectl apply -f config/oauth2-proxy.yaml

#Wait for the keycloak pod to be fully set up 
kubectl rollout status --watch --timeout=600s statefulset/keycloak

kubectl get all

echo "Everthing set up and ready"
echo "If this is a first time install login to the keycloak ui to create a new user"
echo "Keycloak: http://keycloak.localtest.me"
echo "Spring  : http://localtest.me"
