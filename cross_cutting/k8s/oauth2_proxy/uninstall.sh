docker image rm spring-server -f 

helm uninstall keycloak

helm uninstall ingress-nginx -n ingress-nginx

helm repo remove bitnami
helm repo remove ingress-nginx

kubectl delete -f config/ingress.yaml

kubectl delete configmap keycloak-realm

kubectl delete -f config/springServer.yaml

kubectl delete -f config/oauth2-proxy.yaml

if [[ $* == *--remove-db* ]]
then
    kubectl delete pvc --all 
fi