# Open FaaS Router and RSocket Streamer

Kafka based OpenFaas function router, which routes the messages to a function, decoratively. It also provides ability to
stream the messages via RSocket. It also supports client side telemetry. This framework is powered by GraalVM and
deployable on k8s. The framework completely cloud agnostic and native. Function support is language agnostic.

The router has ability to listen to wild card based topic name and dispatch messages stream to function. FaaS provides
on demand the scalable platform with "zero" line of infrastructure as code. Also the container on which functions runs,
support varieties of configurable environments, ranging from python to java to nodejs and more.

The goal of this framework is to provide relief from a batch oriented framework without compromising on the volume of data
one can process. It will enable end user to think "function" nas building block for processing. This framework will
be great help for micro-service orchestration or data (service) orchestration. It uses optioned programing model. Three
fundamental block of this framework are:
- Source
- Processor
- Sink

It worth mentioning, the framework is inspired from "Pipeline architecture". The message transport layer is configurable,
currently kafka used.


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

