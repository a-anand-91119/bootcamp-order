package dev.notyouraverage.bootcamp_order.document_db.services;

import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import java.util.List;

public interface OrderEventService {

    // Event operations
    OrderEventDocumentDTO createOrderEvent(CreateOrderEventRequest request);

    List<OrderEventDocumentDTO> getEventsByOrderId(String orderId);

    List<OrderEventDocumentDTO> getEventsByOrderIdUpToVersion(String orderId, Integer maxVersion);

    List<OrderEventDocumentDTO> getEventsByType(OrderEventType eventType);

    // State projection operations
    OrderStateDTO getOrderCurrentState(String orderId);

    OrderStateDTO getOrderStateAtVersion(String orderId, Integer version);

    List<OrderStateDTO> getOrderStatesByCustomerId(String customerId);

    // Soft delete operation (creates DELETE event)
    OrderEventDocumentDTO deleteOrder(String orderId, String eventSource);

    // Check if order exists (has events)
    boolean orderExists(String orderId);
}
