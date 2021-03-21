kubectl apply -k github.com/minio/operator/\?ref\=v4.0.3
echo "Waiting for minio to deploy"
sleep 10
echo "Create a minio tenant"
kubectl apply -f tenant.yaml
echo "Waiting for tenant to deploy"
sleep 100
echo "Status minio"
kubectl get pods -n minio-operator
# check minio pods running
echo "Check all pods are running ....."
kubectl get pods
# check for initialized status
echo "Make sure tenant is initialized..."
watch -n 0.2 kubectl get tenants.minio.min.io
# accessable service
# https://minio.default.svc.cluster.local
# service name = minio
# default = namespace
# UI url https://play.min.io
