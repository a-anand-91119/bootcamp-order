package dev.notyouraverage.bootcamp_order.kafka_basics.services.impl;

import dev.notyouraverage.bootcamp_order.kafka_basics.constants.KafkaConstants;
import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.kafka_basics.services.KafkaService;
import dev.notyouraverage.bootcamp_order.kafka_basics.transformers.OrderEventTransformer;
import dev.notyouraverage.commons.utils.CompletableFutureUtils;
import dev.notyouraverage.messages.JsonSerializable;
import dev.notyouraverage.messages.OrderCreated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, JsonSerializable> kafkaTemplate;

    private final OrderEventTransformer orderEventTransformer;

    private final String orderCreatedTopic;

    public KafkaServiceImpl(
            @Qualifier(KafkaConstants.JSON_SERIALIZABLE_KAFKA_TEMPLATE) KafkaTemplate<String, JsonSerializable> kafkaTemplate,
            OrderEventTransformer orderEventTransformer,
            @Value("${app.kafka.topics.order_created}") String orderCreatedTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventTransformer = orderEventTransformer;
        this.orderCreatedTopic = orderCreatedTopic;
    }

    @Override
    public void send(CreateOrderRequest request) {
        OrderCreated orderCreated = orderEventTransformer.toOrderCreatedEvent(request);
        CompletableFutureUtils.unchekedGet(kafkaTemplate.send(orderCreatedTopic, orderCreated));
    }
}
