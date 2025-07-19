package dev.notyouraverage.bootcamp_order.kafka_basics.listeners;

import dev.notyouraverage.bootcamp_order.kafka_basics.constants.KafkaConstants;
import dev.notyouraverage.messages.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedListener {

    @KafkaListener(id = KafkaConstants.ORDER_CREATED_LISTENER_ID, groupId = KafkaConstants.ORDER_CREATED_LISTENER, topics = "${app.kafka.topics.order_created}", containerFactory = KafkaConstants.JSON_SERIALIZABLE_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public void handleOrderCreated(OrderCreated orderCreated) {
        log.info("Received OrderCreated message: {}", orderCreated);
    }
}
