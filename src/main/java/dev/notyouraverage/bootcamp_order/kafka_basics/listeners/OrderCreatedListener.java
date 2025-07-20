package dev.notyouraverage.bootcamp_order.kafka_basics.listeners;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import dev.notyouraverage.messages.json.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedListener {

    @KafkaListener(id = KafkaConstants.ORDER_CREATED_LISTENER_ID, groupId = KafkaConstants.ORDER_CREATED_LISTENER_GROUP, topics = "${app.kafka.topics.order_created}", containerFactory = KafkaConstants.JSON_SERIALIZABLE_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public void handleOrderCreated(
            @Payload OrderCreated orderCreated,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Received Json message from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        log.info("Received OrderCreated message: {}", orderCreated);
    }
}
