# Open FaaS Router and RSocket Streamer

  Kafka based OpenFaas function router, which routes the messages to a function, decoratively. It also provides ability
  to stream the messages via RSocket. It also supports client side telemetry. This framework is powered by GraalVM and 
  deployable on k8s. The framework completely cloud agnostic and native. Function support is language agnostic.
  
  The router listens has ability to listen to wild card based topic name and dispatch messages to function.

# Tech stack
- Java 11
- GraalVM
- Maven
- OpenFaas
- Spring Cloud
- Spring native
- Apache Camel
- Minio (aws s3 compatibile support)
- Kafka
- Kubernetes
- Jkube

