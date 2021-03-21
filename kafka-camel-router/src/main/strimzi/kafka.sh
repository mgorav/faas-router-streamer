echo "Clean existing docker images.."
docker-clean all
echo "Create k8s cluster with 1 node...."
k3d cluster create
sleep 20
echo "Creating namespace for kafka...."
kubectl create namespace kafka
echo "Starting kafka deployment ..."
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
sleep 10
kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka
echo "Waiting for kafka and zookeeper to come up...."
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka
echo "Creating master channel..."
kubectl apply -f kafkatopic.yml -n kafka
echo "Starting the kafka producer...."
# kubectl -n kafka run kafka-producer -ti --image=quay.io/strimzi/kafka:0.21.1-kafka-2.7.0 --rm=true --restart=Never -- bin/kafka-console-producer.sh --broker-list my-cluster-kafka-bootstrap:9092 --topic master-channel
