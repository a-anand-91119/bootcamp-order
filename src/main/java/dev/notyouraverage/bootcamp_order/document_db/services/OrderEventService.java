package dev.notyouraverage.bootcamp_order.document_db.services;

import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import java.util.List;

public interface OrderEventService {

    OrderEventDocumentDTO createOrderEvent(CreateOrderEventRequest request);

    List<OrderEventDocumentDTO> getEventsByOrderId(String orderId);

    List<OrderEventDocumentDTO> getEventsByOrderIdUpToVersion(String orderId, Integer maxVersion);

    List<OrderEventDocumentDTO> getEventsByType(OrderEventType eventType);

    OrderStateDTO getOrderCurrentState(String orderId);

    OrderStateDTO getOrderStateAtVersion(String orderId, Integer version);

    List<OrderStateDTO> getOrderStatesByCustomerId(String customerId);

    OrderEventDocumentDTO deleteOrder(String orderId, String eventSource);

    boolean orderExists(String orderId);

}
