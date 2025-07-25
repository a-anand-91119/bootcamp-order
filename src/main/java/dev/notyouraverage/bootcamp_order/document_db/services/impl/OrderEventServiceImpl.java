package dev.notyouraverage.bootcamp_order.document_db.services.impl;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.bootcamp_order.document_db.services.OrderEventService;
import dev.notyouraverage.bootcamp_order.document_db.transformers.OrderEventLifecycleTransformer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventServiceImpl implements OrderEventService {

    private final OrderEventRepository orderEventRepository;

    private final OrderEventLifecycleTransformer orderEventLifecycleTransformer;

    @Override
    @Transactional
    public OrderEventDocumentDTO createOrderEvent(CreateOrderEventRequest request) {
        Integer nextVersion = orderEventRepository.getNextVersionForOrder(request.getOrderId());
        OrderEventDocument orderEvent = orderEventLifecycleTransformer.toEventEntity(request, nextVersion);
        OrderEventDocument savedEvent = orderEventRepository.save(orderEvent);

        return orderEventLifecycleTransformer.toEventDocumentDTO(savedEvent);
    }

    @Override
    public List<OrderEventDocumentDTO> getEventsByOrderId(String orderId) {
        List<OrderEventDocument> events = orderEventRepository.findByOrderIdOrderedByVersion(orderId);
        return orderEventLifecycleTransformer.toEventDocumentDTOs(events);
    }

    @Override
    public List<OrderEventDocumentDTO> getEventsByOrderIdUpToVersion(String orderId, Integer maxVersion) {
        List<OrderEventDocument> events = orderEventRepository.findByOrderIdUpToVersion(orderId, maxVersion);
        return orderEventLifecycleTransformer.toEventDocumentDTOs(events);
    }

    @Override
    public List<OrderEventDocumentDTO> getEventsByType(OrderEventType eventType) {
        List<OrderEventDocument> events = orderEventRepository.findByEventType(eventType);
        return orderEventLifecycleTransformer.toEventDocumentDTOs(events);
    }

    @Override
    public OrderStateDTO getOrderCurrentState(String orderId) {
        List<OrderEventDocument> events = orderEventRepository.findByOrderIdOrderedByVersion(orderId);
        if (events.isEmpty()) {
            return null;
        }
        return orderEventLifecycleTransformer.projectOrderState(events);
    }

    @Override
    public OrderStateDTO getOrderStateAtVersion(String orderId, Integer version) {
        List<OrderEventDocument> events = orderEventRepository.findByOrderIdUpToVersion(orderId, version);
        if (events.isEmpty()) {
            return null;
        }
        return orderEventLifecycleTransformer.projectOrderState(events);
    }

    @Override
    public List<OrderStateDTO> getOrderStatesByCustomerId(String customerId) {
        List<OrderEventDocument> allEvents = orderEventRepository.findAllOrderedByOrderIdAndVersion();

        Map<String, List<OrderEventDocument>> eventsByOrderId = allEvents.stream()
                .collect(Collectors.groupingBy(OrderEventDocument::getOrderId));

        return eventsByOrderId.values()
                .stream()
                .map(orderEventLifecycleTransformer::projectOrderState)
                .filter(state -> state != null && customerId.equals(state.getCustomerId()))
                .toList();
    }

    @Override
    @Transactional
    public OrderEventDocumentDTO deleteOrder(String orderId, String eventSource) {
        if (!orderExists(orderId)) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        CreateOrderEventRequest deleteRequest = orderEventLifecycleTransformer
                .createDeleteEventRequest(orderId, eventSource);
        return createOrderEvent(deleteRequest);
    }

    @Override
    public boolean orderExists(String orderId) {
        List<OrderEventDocument> events = orderEventRepository.findByOrderIdOrderedByVersion(orderId);
        return !events.isEmpty();
    }

}
