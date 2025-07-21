package dev.notyouraverage.bootcamp_order.combine_together.listeners;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import dev.notyouraverage.bootcamp_order.document_db.helpers.OrderEventHelper;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderEventHelper orderEventHelper;

    @KafkaListener(id = KafkaConstants.ORDER_AVRO_LISTENER_ID, groupId = KafkaConstants.ORDER_AVRO_LISTENER_GROUP, topics = "${app.kafka.topics.order_avro_events}", containerFactory = KafkaConstants.AVRO_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("Received OrderCreatedEvent for orderId: {}", orderCreatedEvent.getOrderId());
        orderEventHelper.processOrderCreatedEvent(orderCreatedEvent);
    }

}
