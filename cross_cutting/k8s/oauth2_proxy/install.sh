#!/bin/bash

#This is to prevent the scrip from running when port 80 is blocked
#Please make sure that nothing is blocking port 80 beacause if the port is blocked
#by something the ingress controller will not listen on localhost but on an external cluster ip
#We need to listen on localhost beacuse the service localtest.me will resolve as 127.0.0.1 without any modification in host files
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

if [ -z "$dockerimageresult" ]
then
    #Docker images doesn't exist so create it from the dockerfile
    mvn -f ./ProxyTestServer/pom.xml clean install
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
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-nginx --create-namespace


#Wait for ingress controller to be fully running befor creating ingress
#This is needed to prevent an error causing the incress deployment to fail
kubectl wait deployment -n ingress-nginx ingress-nginx-controller --for condition=Available=True --timeout=-1s

#Add an ingress to route all requests to the proxy
#The ingress is using the created nginx controller installed in the prior step
#To test the application localy we use the service localtest.me
#This services is a fully qualified domain name that gets resolved to 127.0.0.1 by public dns servers
kubectl apply -f config/ingress.yaml


#Move the exported realm file from keycloak into a configmap 
#to access it when importing the realm back into keycloak during the deployment
kubectl create configmap keycloak-realm --from-file=config/keycloak/realm.json


#Create keycloak from a helm chart
#Usernames and passwords for keycloak and it's database can be found in the keyCloakValues file
helm upgrade --install keycloak bitnami/keycloak -f config/keyCloakValues.yaml


#Deploy the spring service that is secured behind the proxy
kubectl apply -f config/springServer.yaml


#Add the keyclak proxy to route all requests to
#The proxy will check the request for the nessesery permissions
#If the request is a valid the proxy will redirect the request to the upstream
#If not the browser of the user will redirect him to the oauth screen to login
kubectl apply -f config/oauth2-proxy.yaml

#Wait for the keycloak pod to be fully set up 
kubectl rollout status --watch --timeout=600s statefulset/keycloak

kubectl get all

echo "Everthing set up and ready"
echo "If this is a first time install login to keycloak to create a new user"
echo "Keycloak: http://keycloak.localtest.me"
echo "Spring  : http://localtest.me"
