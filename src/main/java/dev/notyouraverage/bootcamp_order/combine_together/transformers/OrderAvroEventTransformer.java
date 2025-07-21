package dev.notyouraverage.bootcamp_order.combine_together.transformers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.zeplinko.commons.lang.ext.core.Try;

@Slf4j
@Transformer
@RequiredArgsConstructor
public class OrderAvroEventTransformer {

    private final ObjectMapper objectMapper;

    public OrderCreatedEvent toOrderCreatedEvent(Order order) {
        return OrderCreatedEvent.newBuilder()
                .setOrderId(order.getId().toString())
                .setCustomerId(order.getCustomerId().toString())
                .setCustomerEmail(order.getCustomerEmail())
                .setStatus(order.getStatus().name())
                .setOrderDate(order.getOrderDate().atZone(ZoneId.systemDefault()).toInstant())
                .setTotalAmount(decimalToString(order.getTotalAmount()))
                .setCurrency(order.getCurrency())
                .setLineItems(convertLineItems(order.getLineItems()))
                .setShippingAddress(mapToJsonString(order.getShippingAddress()))
                .setSource(order.getSource().name())
                .setChannel(order.getChannel() != null ? order.getChannel().name() : null)
                .setEventTimestamp(Instant.now())
                .setEventSource("order-service")
                .setEventVersion(1)
                .build();
    }

    private List<dev.notyouraverage.messages.avro.OrderLineItem> convertLineItems(List<OrderLineItem> lineItems) {
        if (lineItems == null || lineItems.isEmpty()) {
            return List.of();
        }

        return lineItems.stream()
                .map(this::convertLineItem)
                .collect(Collectors.toList());
    }

    private dev.notyouraverage.messages.avro.OrderLineItem convertLineItem(OrderLineItem lineItem) {
        return dev.notyouraverage.messages.avro.OrderLineItem.newBuilder()
                .setProductId(lineItem.getProductId())
                .setProductName(lineItem.getProductName())
                .setProductSku(lineItem.getProductSku())
                .setQuantity(lineItem.getQuantity())
                .setUnitPrice(decimalToString(lineItem.getUnitPrice()))
                .setTotalPrice(decimalToString(lineItem.getTotalPrice()))
                .build();
    }

    private String decimalToString(BigDecimal decimal) {
        return Optional.ofNullable(decimal)
                .map(BigDecimal::toString)
                .orElse("0.00");
    }

    private String mapToJsonString(Object map) {
        return Optional.ofNullable(map)
                .map(
                        object -> Try.to(() -> objectMapper.writeValueAsString(map))
                                .onFailure(e -> log.warn("Failed to convert map to JSON string: {}", e.getMessage()))
                                .orElse(null)
                )
                .orElse(null);
    }
}
