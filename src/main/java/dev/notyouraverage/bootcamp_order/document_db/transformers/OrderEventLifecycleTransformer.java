package dev.notyouraverage.bootcamp_order.document_db.transformers;

import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import dev.notyouraverage.bootcamp_order.document_db.enums.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Transformer
public class OrderEventLifecycleTransformer {

    public OrderEventDocument toEventEntity(CreateOrderEventRequest request, Integer eventVersion) {
        return OrderEventDocument.builder()
                .id(UUID.randomUUID().toString())
                .orderId(request.getOrderId())
                .eventType(request.getEventType())
                .payload(request.getPayload())
                .timestamp(request.getTimestamp())
                .eventSource(request.getEventSource())
                .eventVersion(eventVersion)
                .documentStatus(Status.ACTIVE)
                .build();
    }

    public OrderEventDocumentDTO toEventDocumentDTO(OrderEventDocument orderEvent) {
        return OrderEventDocumentDTO.builder()
                .id(orderEvent.getId())
                .orderId(orderEvent.getOrderId())
                .eventType(orderEvent.getEventType())
                .payload(orderEvent.getPayload())
                .timestamp(orderEvent.getTimestamp())
                .eventSource(orderEvent.getEventSource())
                .eventVersion(orderEvent.getEventVersion())
                .createdAt(orderEvent.getCreatedAt())
                .updatedAt(orderEvent.getUpdatedAt())
                .version(orderEvent.getVersion())
                .build();
    }

    public List<OrderEventDocumentDTO> toEventDocumentDTOs(List<OrderEventDocument> events) {
        if (events == null) {
            return List.of();
        }
        return events.stream()
                .map(this::toEventDocumentDTO)
                .toList();
    }

    public OrderStateDTO projectOrderState(List<OrderEventDocument> events) {
        if (events == null || events.isEmpty()) {
            return null;
        }

        String orderId = events.getFirst().getOrderId();
        OrderStatus currentStatus = OrderStatus.CREATED;
        String customerId = null;
        LocalDateTime orderCreatedAt = null;
        LocalDateTime lastUpdatedAt = null;
        int currentEventVersion = 0;
        Map<String, Object> currentPayload = null;
        boolean isDeleted = false;

        for (OrderEventDocument event : events) {
            currentEventVersion = Math.max(currentEventVersion, event.getEventVersion());
            lastUpdatedAt = event.getTimestamp();

            switch (event.getEventType()) {
                case ORDER_CREATED:
                    currentStatus = OrderStatus.CREATED;
                    orderCreatedAt = event.getTimestamp();
                    if (event.getPayload() != null) {
                        customerId = (String) event.getPayload().get("customerId");
                        currentPayload = event.getPayload();
                    }
                    break;
                case ORDER_UPDATED:
                    if (event.getPayload() != null) {
                        currentPayload = event.getPayload();
                        Object statusObj = event.getPayload().get("orderStatus");
                        if (statusObj instanceof String) {
                            try {
                                currentStatus = OrderStatus.valueOf((String) statusObj);
                            } catch (IllegalArgumentException _) {
                            }
                        }
                    }
                    break;
                case ORDER_PAID:
                    currentStatus = OrderStatus.PAID;
                    break;
                case ORDER_SHIPPED:
                    currentStatus = OrderStatus.SHIPPED;
                    break;
                case ORDER_CANCELLED:
                    currentStatus = OrderStatus.CANCELLED;
                    break;
                case ORDER_DELETED:
                    isDeleted = true;
                    break;
                case ORDER_UPDATED_STATUS:
                case ORDER_UPDATED_STATUS_TO_PAID:
                    if (event.getPayload() != null) {
                        Object statusObj = event.getPayload().get("orderStatus");
                        if (statusObj instanceof String) {
                            try {
                                currentStatus = OrderStatus.valueOf((String) statusObj);
                            } catch (IllegalArgumentException _) {
                            }
                        }
                    }
                    break;
            }
        }

        return OrderStateDTO.builder()
                .orderId(orderId)
                .currentStatus(currentStatus)
                .customerId(customerId)
                .orderCreatedAt(orderCreatedAt)
                .lastUpdatedAt(lastUpdatedAt)
                .currentEventVersion(currentEventVersion)
                .currentPayload(currentPayload)
                .events(toEventDocumentDTOs(events))
                .isDeleted(isDeleted)
                .build();
    }

    public CreateOrderEventRequest createDeleteEventRequest(String orderId, String eventSource) {
        return CreateOrderEventRequest.builder()
                .orderId(orderId)
                .eventType(OrderEventType.ORDER_DELETED)
                .payload(Map.of("reason", "Order deleted"))
                .timestamp(LocalDateTime.now())
                .eventSource(eventSource)
                .build();
    }
}
