package com.gonnect.kafka.camel.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

@Controller
@Log4j2
public class KafkaStreamer {
    private final KafkaListener kafkaListener;

    public KafkaStreamer(KafkaListener kafkaListener) {
        this.kafkaListener = kafkaListener;
    }

    @MessageMapping("stream")
    public Flux<String> stream(RSocketRequester clientRequester, String topicName) {
        log.info("Requesting for stream for " + topicName);
        kafkaListener.registerStreamSupplier(topicName, new KafkaStreamSupplier<>());
        // Telemetry support. If client is not healthy, no need to send stream
        var in = clientRequester
                .route("health")
                .retrieveFlux(State.class)
                .filter(health -> !health.isHealthy());
        // empty in case of healthy
        // State:healthy == false => Not empty

        var out = Flux
                .fromStream(Stream.generate(kafkaListener.getStreamPublisher(topicName)))
                .delayElements(Duration.ofSeconds(1));

        return out.takeUntilOther(in);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class State {
        private boolean healthy;
    }
}
