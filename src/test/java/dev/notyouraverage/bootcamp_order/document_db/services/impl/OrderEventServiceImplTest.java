package dev.notyouraverage.bootcamp_order.document_db.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import dev.notyouraverage.bootcamp_order.document_db.enums.Status;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.bootcamp_order.document_db.transformers.OrderEventLifecycleTransformer;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderEventServiceImplTest {

    @Mock
    private OrderEventRepository orderEventRepository;

    private OrderEventLifecycleTransformer orderEventLifecycleTransformer;

    private OrderEventServiceImpl orderEventService;

    private final String TEST_ORDER_ID = "order-123";

    private final String TEST_CUSTOMER_ID = "customer-123";

    private final String TEST_EVENT_SOURCE = "order-service";

    private final LocalDateTime TEST_TIMESTAMP = LocalDateTime.of(2023, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        orderEventLifecycleTransformer = new OrderEventLifecycleTransformer();
        orderEventService = new OrderEventServiceImpl(orderEventRepository, orderEventLifecycleTransformer);
    }

    @Test
    void createOrderEvent_ShouldCreateAndReturnEvent() {
        // Given
        CreateOrderEventRequest request = new CreateOrderEventRequest();
        request.setOrderId(TEST_ORDER_ID);
        request.setEventType(OrderEventType.ORDER_CREATED);
        request.setPayload(Map.of("customerId", TEST_CUSTOMER_ID, "amount", 100.0));
        request.setTimestamp(TEST_TIMESTAMP);
        request.setEventSource(TEST_EVENT_SOURCE);

        OrderEventDocument savedEvent = OrderEventDocument.builder()
                .id("event-123")
                .orderId(TEST_ORDER_ID)
                .eventType(OrderEventType.ORDER_CREATED)
                .payload(request.getPayload())
                .timestamp(TEST_TIMESTAMP)
                .eventSource(TEST_EVENT_SOURCE)
                .eventVersion(1)
                .documentStatus(Status.ACTIVE)
                .build();

        when(orderEventRepository.getNextVersionForOrder(TEST_ORDER_ID)).thenReturn(1);
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(savedEvent);

        // When
        OrderEventDocumentDTO result = orderEventService.createOrderEvent(request);

        // Then
        assertNotNull(result);
        assertEquals("event-123", result.getId());
        assertEquals(TEST_ORDER_ID, result.getOrderId());
        assertEquals(OrderEventType.ORDER_CREATED, result.getEventType());
        assertEquals(1, result.getEventVersion());
        assertEquals(TEST_EVENT_SOURCE, result.getEventSource());

        verify(orderEventRepository).getNextVersionForOrder(TEST_ORDER_ID);
        verify(orderEventRepository).save(any(OrderEventDocument.class));
    }

    @Test
    void getEventsByOrderId_ShouldReturnOrderedEvents() {
        // Given
        List<OrderEventDocument> events = Arrays.asList(
                createTestEvent("event-1", 1, OrderEventType.ORDER_CREATED),
                createTestEvent("event-2", 2, OrderEventType.ORDER_PAID)
        );

        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(events);

        // When
        List<OrderEventDocumentDTO> result = orderEventService.getEventsByOrderId(TEST_ORDER_ID);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(OrderEventType.ORDER_CREATED, result.get(0).getEventType());
        assertEquals(OrderEventType.ORDER_PAID, result.get(1).getEventType());

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
    }

    @Test
    void getOrderCurrentState_ShouldProjectCorrectState() {
        // Given
        List<OrderEventDocument> events = Arrays.asList(
                createTestEventWithPayload(
                        "event-1",
                        1,
                        OrderEventType.ORDER_CREATED,
                        Map.of("customerId", TEST_CUSTOMER_ID, "amount", 100.0)
                ),
                createTestEvent("event-2", 2, OrderEventType.ORDER_PAID)
        );

        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(events);

        // When
        OrderStateDTO result = orderEventService.getOrderCurrentState(TEST_ORDER_ID);

        // Then
        assertNotNull(result);
        assertEquals(TEST_ORDER_ID, result.getOrderId());
        assertEquals(OrderStatus.PAID, result.getCurrentStatus());
        assertEquals(TEST_CUSTOMER_ID, result.getCustomerId());
        assertEquals(2, result.getCurrentEventVersion());
        assertEquals(2, result.getEvents().size());
        assertFalse(result.isDeleted());

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
    }

    @Test
    void deleteOrder_ShouldCreateDeleteEvent() {
        // Given
        List<OrderEventDocument> existingEvents = Arrays.asList(
                createTestEvent("event-1", 1, OrderEventType.ORDER_CREATED)
        );

        OrderEventDocument deleteEvent = createTestEvent("delete-event", 2, OrderEventType.ORDER_DELETED);

        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(existingEvents);
        when(orderEventRepository.getNextVersionForOrder(TEST_ORDER_ID)).thenReturn(2);
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(deleteEvent);

        // When
        OrderEventDocumentDTO result = orderEventService.deleteOrder(TEST_ORDER_ID, TEST_EVENT_SOURCE);

        // Then
        assertNotNull(result);
        assertEquals(OrderEventType.ORDER_DELETED, result.getEventType());
        assertEquals(2, result.getEventVersion());

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
        verify(orderEventRepository).getNextVersionForOrder(TEST_ORDER_ID);
        verify(orderEventRepository).save(any(OrderEventDocument.class));
    }

    @Test
    void deleteOrder_WhenOrderNotExists_ShouldThrowException() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(Collections.emptyList());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderEventService.deleteOrder(TEST_ORDER_ID, TEST_EVENT_SOURCE)
        );

        assertEquals("Order not found with id: " + TEST_ORDER_ID, exception.getMessage());

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
        verify(orderEventRepository, never()).save(any());
    }

    @Test
    void orderExists_WhenOrderHasEvents_ShouldReturnTrue() {
        // Given
        List<OrderEventDocument> events = Arrays.asList(
                createTestEvent("event-1", 1, OrderEventType.ORDER_CREATED)
        );

        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(events);

        // When
        boolean result = orderEventService.orderExists(TEST_ORDER_ID);

        // Then
        assertTrue(result);

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
    }

    @Test
    void orderExists_WhenOrderHasNoEvents_ShouldReturnFalse() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(TEST_ORDER_ID)).thenReturn(Collections.emptyList());

        // When
        boolean result = orderEventService.orderExists(TEST_ORDER_ID);

        // Then
        assertFalse(result);

        verify(orderEventRepository).findByOrderIdOrderedByVersion(TEST_ORDER_ID);
    }

    private OrderEventDocument createTestEvent(String eventId, Integer version, OrderEventType eventType) {
        return createTestEventWithPayload(eventId, version, eventType, Map.of());
    }

    private OrderEventDocument createTestEventWithPayload(
            String eventId,
            Integer version,
            OrderEventType eventType,
            Map<String, Object> payload
    ) {
        return OrderEventDocument.builder()
                .id(eventId)
                .orderId(TEST_ORDER_ID)
                .eventType(eventType)
                .payload(payload)
                .timestamp(TEST_TIMESTAMP.plusMinutes(version))
                .eventSource(TEST_EVENT_SOURCE)
                .eventVersion(version)
                .documentStatus(Status.ACTIVE)
                .build();
    }
}
