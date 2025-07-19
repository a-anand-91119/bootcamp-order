package dev.notyouraverage.bootcamp_order.kafka_basics.services;

import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateOrderRequest;

public interface KafkaService {
    void send(CreateOrderRequest request);
}
