package com.gonnect.kafka.camel.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static java.util.UUID.randomUUID;

@Component
public class DynamicKafkaRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        log.info("About to start route: Kafka Server -> Log ");
        from("kafka:{{consumer.topic}}?brokers={{kafka.host}}:{{kafka.port}}"
                + "&maxPollRecords={{consumer.maxPollRecords}}"
                + "&consumersCount={{consumer.consumersCount}}"
                + "&seekTo={{consumer.seekTo}}"
                + "&groupId={{consumer.group}}"
                + "&topicIsPattern={{consumer.topicIsPattern}}")
                .routeId(format("FromKafka-{0}", randomUUID()))
                .transform()
                .method("kafka-listener", "listen")
                .log("${body}")
                .log(">>>>> on the topic ${headers[kafka.TOPIC]}")
                .log(">>>>> on the partition ${headers[kafka.PARTITION]}")
                .log(">>>>> with the offset ${headers[kafka.OFFSET]}")
                .log(">>>>> with the key ${headers[kafka.KEY]}");
    }
}
