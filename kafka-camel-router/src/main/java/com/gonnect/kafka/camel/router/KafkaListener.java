package com.gonnect.kafka.camel.router;

import lombok.extern.log4j.Log4j2;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("kafka-listener")
@Log4j2
public class KafkaListener<T> {
    private final WebClient client;
    private final K8sService k8sService;
    private final Map<String, KafkaStreamSupplier<T>> topicPublisher;

    public KafkaListener() {
        this.client = WebClient.builder()
                .baseUrl("http://gateway.openfaas:8080/function")
                .clientConnector(new ReactorClientHttpConnector())
                .build();
        k8sService = new K8sService();
        topicPublisher = new HashMap<>();
    }

    public void listen(@Header("kafka.TOPIC") String topicName, @Body String routableMessage) {
        log.info("Received routable message: {}.", routableMessage);

        if (topicPublisher.containsKey(topicName)) {
            log.info("Publishing routable message: {}.", routableMessage);
            topicPublisher.get(topicName).offer((T) routableMessage);
        }

        List<String> functionsBoundToATopic = k8sService.getAnnotatedFunctions();

        if (!functionsBoundToATopic.isEmpty()) {
            functionsBoundToATopic.forEach(function -> {
                invokeFunction(routableMessage, function);
            });
        }
    }


    public void registerStreamSupplier(String topicName, KafkaStreamSupplier<T> kafkaStreamSupplier) {
        if (!topicPublisher.containsKey(topicName)) {
            log.info("Registering for stream access " + topicName);
            topicPublisher.put(topicName, kafkaStreamSupplier);
        }
    }

    public KafkaStreamSupplier<?> getStreamPublisher(String topicName) {
        return topicPublisher.get(topicName);
    }


    private void invokeFunction(String routableMessage, String functionName) {
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(
                uriBuilder -> uriBuilder.pathSegment(functionName).build());
        WebClient.RequestHeadersSpec headersSpec = bodySpec.body(
                BodyInserters.fromPublisher(Mono.just(routableMessage), String.class)
        );
        log.info("Invoking function {}", functionName);

        Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);

        // TODO: In true spirit of reactive make the following log enabled in case debug
        log.info("Received message from function {}: {}", functionName, response.block());
    }
}
