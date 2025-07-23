package dev.notyouraverage.bootcamp_order.kafka_basics.services;

import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateJsonPayloadOrderRequest;

public interface KafkaService {
    void send(CreateJsonPayloadOrderRequest request);
}
