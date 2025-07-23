package dev.notyouraverage.bootcamp_order.kafka_basics.transformers;

import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateJsonPayloadOrderRequest;
import dev.notyouraverage.messages.json.OrderCreated;

@Transformer
public class OrderEventTransformer {

    public OrderCreated toOrderCreatedEvent(CreateJsonPayloadOrderRequest request) {
        return OrderCreated.builder()
                .message(request.getMessage())
                .build();
    }
}
