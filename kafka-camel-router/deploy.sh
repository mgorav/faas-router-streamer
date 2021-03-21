echo "Creating namespace for the router ...."
kubectl create namespace fn-router
echo "Creating native image. This will take few minutes ..."
#mvn clean install spring-boot:build-image -DskipTests
echo "Deploying to kubernetes"
mvn k8s:undeploy k8s:build k8s:push k8s:resource k8s:apply k8s:deploy -DskipTests -Pkubernetes
echo "Tailing logs ..."
mvn k8s:log -Pkubernetes

