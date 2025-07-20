package dev.notyouraverage.bootcamp_order.kafka_basics.transformers;

import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateOrderRequest;
import dev.notyouraverage.messages.json.OrderCreated;

@Transformer
public class OrderEventTransformer {

    public OrderCreated toOrderCreatedEvent(CreateOrderRequest request) {
        return OrderCreated.builder()
                .message(request.getMessage())
                .build();
    }
}
