package com.gonnect.rsocket.streamer.client;

import io.rsocket.SocketAcceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

@Configuration
@Log4j2
public class Configurer {

    @Bean
    public SocketAcceptor socketAcceptor(RSocketStrategies strategies, StreamerHealthController controller) {
        return RSocketMessageHandler
                .responder(strategies, controller);
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder, SocketAcceptor acceptor) {
        return builder
                .rsocketConnector(connector -> {
                    connector.acceptor(acceptor);
                })
                .connectTcp("localhost", 5555)
                .block();
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> streamerClient(RSocketRequester requester) {
        return arguments -> {
            var flux = requester
                    .route("stream")
                    .data("master-channel")
                    .retrieveFlux(String.class);
            flux.subscribe(message -> {
                log.info(message);
            }, error -> {
                log.error(error);
            });
        };
    }
}

