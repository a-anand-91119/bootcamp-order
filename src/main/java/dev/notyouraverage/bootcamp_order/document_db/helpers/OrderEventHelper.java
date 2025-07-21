package dev.notyouraverage.bootcamp_order.document_db.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.notyouraverage.base.annotations.ServiceHelper;
import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ServiceHelper
@RequiredArgsConstructor
public class OrderEventHelper {
    private final OrderEventRepository orderEventRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public void processOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("Processing OrderCreatedEvent for orderId: {}", orderCreatedEvent.getOrderId());

        try {
            OrderEventDocument eventDocument = OrderEventDocument.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .orderId(orderCreatedEvent.getOrderId())
                    .eventType(OrderEventType.ORDER_CREATED)
                    .payload(convertToPayload(orderCreatedEvent))
                    .timestamp(
                            LocalDateTime.ofInstant(
                                    orderCreatedEvent.getEventTimestamp(),
                                    ZoneId.systemDefault()
                            )
                    )
                    .eventSource(orderCreatedEvent.getEventSource())
                    .eventVersion(orderCreatedEvent.getEventVersion())
                    .build();

            OrderEventDocument saved = orderEventRepository.save(eventDocument);
            log.info("Successfully saved OrderCreatedEvent to MongoDB with id: {}", saved.getMongoId());

        } catch (Exception e) {
            log.error(
                    "Failed to process OrderCreatedEvent for orderId: {}",
                    orderCreatedEvent.getOrderId(),
                    e
            );
            throw e;
        }
    }

    private Map<String, Object> convertToPayload(OrderCreatedEvent event) {
        try {
            String jsonString = event.toString();
            return objectMapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            log.warn("Failed to convert Avro event to Map, using manual conversion", e);
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", event.getOrderId());
            payload.put("customerId", event.getCustomerId());
            payload.put("customerEmail", event.getCustomerEmail() != null ? event.getCustomerEmail() : null);
            payload.put("status", event.getStatus());
            payload.put("orderDate", event.getOrderDate());
            payload.put("totalAmount", event.getTotalAmount());
            payload.put("currency", event.getCurrency());
            payload.put("source", event.getSource());
            payload.put("channel", event.getChannel() != null ? event.getChannel() : null);
            payload.put("shippingAddress", event.getShippingAddress() != null ? event.getShippingAddress() : null);
            payload.put("lineItemCount", event.getLineItems().size());
            return payload;
        }
    }

}
