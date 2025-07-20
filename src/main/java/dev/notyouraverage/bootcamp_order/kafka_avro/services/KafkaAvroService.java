package dev.notyouraverage.bootcamp_order.kafka_avro.services;

import dev.notyouraverage.messages.avro.User;

public interface KafkaAvroService {
    void sendUser(User user);
}
