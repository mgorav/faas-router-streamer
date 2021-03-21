package com.gonnect.rsocket.streamer.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

@Controller
public class StreamerHealthController {

    @MessageMapping("health")
    Flux<State> healthy() {
        var stream = Stream.generate(() -> new State(Math.random() > 0.2));
        return Flux
                .fromStream(stream)
                .delayElements(Duration.ofSeconds(1));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class State {
        private boolean healthy;
    }


}


