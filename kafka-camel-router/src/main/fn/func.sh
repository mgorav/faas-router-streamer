#faas-cli new --lang python3 s3-fun --prefix="mgorav"

kubectl apply -f https://raw.githubusercontent.com/openfaas/faas-netes/master/namespaces.yml
helm repo add openfaas https://openfaas.github.io/faas-netes/
helm repo update &&
  helm upgrade openfaas --install openfaas/openfaas \
    --namespace openfaas \
    --set functionNamespace=openfaas-fn \
    --set generateBasicAuth=true \
    --set openfaasPRO=false --set serviceType=LoadBalancer --set ingress.enabled=true

echo "Waiting for OpenFaas to deploy ......."
sleep 4
kubectl -n openfaas get deployments -l "release=openfaas, app=openfaas"


PASSWORD=$(kubectl -n openfaas get secret basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode) &&
  echo "OpenFaaS admin password: $PASSWORD"
echo $(kubectl -n openfaas get secret basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode)
export OPENFAAS_URL=http://127.0.0.1:8080
echo "Port forwardng 8080 ....."
kubectl port-forward -n openfaas svc/gateway 8080:8080 &
echo -n $PASSWORD | faas-cli login -g $OPENFAAS_URL -u admin --password-stdin
faas-cli login --password $PASSWORD
faas-cli version

faas-cli up -f *.yml

echo "Function deployed status..."
 kubectl get pods -n openfaas-fn
