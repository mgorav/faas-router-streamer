pkill kubectl -9

kubectl port-forward service/kafka-camel-router 5555:8888 &

#rsc tcp://localhost:5555 --stream --route stream --log --debug -d "master-channel"
