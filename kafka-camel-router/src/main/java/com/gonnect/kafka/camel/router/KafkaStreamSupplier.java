package com.gonnect.kafka.camel.router;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

@Log4j2
public class KafkaStreamSupplier<T> implements Supplier<T> {
    private final Queue<T> queue;

    public KafkaStreamSupplier() {
        this.queue = new LinkedList<>();
    }

    @Override
    public T get() {
        T message = this.queue.poll();
        log.info("Message being streamed: " + message);
        return message == null ? (T) "NULL" : message;
    }

    public void offer(T routableMessage) {
        this.queue.offer(routableMessage);
    }
}
