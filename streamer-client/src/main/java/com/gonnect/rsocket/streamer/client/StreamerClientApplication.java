package com.gonnect.rsocket.streamer.client;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamerClientApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(StreamerClientApplication.class, args);
        System.in.read();
    }

}
