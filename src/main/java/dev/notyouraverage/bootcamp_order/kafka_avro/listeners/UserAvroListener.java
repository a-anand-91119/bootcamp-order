package dev.notyouraverage.bootcamp_order.kafka_avro.listeners;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import dev.notyouraverage.messages.avro.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAvroListener {

    @KafkaListener(id = KafkaConstants.USER_AVRO_LISTENER_ID, groupId = KafkaConstants.USER_AVRO_LISTENER_GROUP, topics = "${app.kafka.topics.user_avro_message}", containerFactory = KafkaConstants.AVRO_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public void listen(
            @Payload User user,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Received Avro User message from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        log.info(
                "User details - firstName: {}, lastName: {}, phoneNumber: {}",
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
    }
}
