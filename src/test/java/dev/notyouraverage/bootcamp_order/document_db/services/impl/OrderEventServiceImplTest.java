package dev.notyouraverage.bootcamp_order.document_db.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.bootcamp_order.document_db.transformers.OrderEventLifecycleTransformer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderEventServiceImplTest {

    @Mock
    private OrderEventRepository orderEventRepository;

    @Mock
    private OrderEventLifecycleTransformer orderEventLifecycleTransformer;

    @InjectMocks
    private OrderEventServiceImpl orderEventService;

    private CreateOrderEventRequest createOrderEventRequest;

    private OrderEventDocument orderEventDocument;

    private OrderEventDocumentDTO orderEventDocumentDTO;

    private OrderStateDTO orderStateDTO;

    private String orderId;

    private String eventSource;

    @BeforeEach
    void setUp() {
        orderId = "order-123";
        eventSource = "order-service";

        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", "customer-456");
        payload.put("totalAmount", "100.00");

        createOrderEventRequest = CreateOrderEventRequest.builder()
                .orderId(orderId)
                .eventType(OrderEventType.ORDER_CREATED)
                .payload(payload)
                .eventSource(eventSource)
                .build();

        orderEventDocument = OrderEventDocument.builder()
                .mongoId("mongo-id-123")
                .orderId(orderId)
                .eventType(OrderEventType.ORDER_CREATED)
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .eventSource(eventSource)
                .eventVersion(1)
                .build();

        orderEventDocumentDTO = OrderEventDocumentDTO.builder()
                .id("mongo-id-123")
                .orderId(orderId)
                .eventType(OrderEventType.ORDER_CREATED)
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .eventSource(eventSource)
                .eventVersion(1)
                .build();

        orderStateDTO = OrderStateDTO.builder()
                .orderId(orderId)
                .currentStatus(OrderStatus.CREATED)
                .customerId("customer-456")
                .currentEventVersion(1)
                .currentPayload(payload)
                .isDeleted(false)
                .build();
    }

    @Test
    void createOrderEvent_ShouldCreateAndSaveEvent_WhenValidRequest() {
        // Given
        when(orderEventRepository.getNextVersionForOrder(orderId)).thenReturn(1);
        when(orderEventLifecycleTransformer.toEventEntity(createOrderEventRequest, 1))
                .thenReturn(orderEventDocument);
        when(orderEventRepository.save(orderEventDocument)).thenReturn(orderEventDocument);
        when(orderEventLifecycleTransformer.toEventDocumentDTO(orderEventDocument))
                .thenReturn(orderEventDocumentDTO);

        // When
        OrderEventDocumentDTO result = orderEventService.createOrderEvent(createOrderEventRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getEventType()).isEqualTo(OrderEventType.ORDER_CREATED);

        verify(orderEventRepository).getNextVersionForOrder(orderId);
        verify(orderEventLifecycleTransformer).toEventEntity(createOrderEventRequest, 1);
        verify(orderEventRepository).save(orderEventDocument);
        verify(orderEventLifecycleTransformer).toEventDocumentDTO(orderEventDocument);
    }

    @Test
    void getEventsByOrderId_ShouldReturnOrderedEvents_WhenEventsExist() {
        // Given
        List<OrderEventDocument> events = List.of(orderEventDocument);
        List<OrderEventDocumentDTO> eventDTOs = List.of(orderEventDocumentDTO);

        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(events);
        when(orderEventLifecycleTransformer.toEventDocumentDTOs(events)).thenReturn(eventDTOs);

        // When
        List<OrderEventDocumentDTO> result = orderEventService.getEventsByOrderId(orderId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(orderId);

        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
        verify(orderEventLifecycleTransformer).toEventDocumentDTOs(events);
    }

    @Test
    void getOrderCurrentState_ShouldReturnProjectedState_WhenEventsExist() {
        // Given
        List<OrderEventDocument> events = List.of(orderEventDocument);

        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(events);
        when(orderEventLifecycleTransformer.projectOrderState(events)).thenReturn(orderStateDTO);

        // When
        OrderStateDTO result = orderEventService.getOrderCurrentState(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getCurrentStatus()).isEqualTo(OrderStatus.CREATED);

        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
        verify(orderEventLifecycleTransformer).projectOrderState(events);
    }

    @Test
    void getOrderCurrentState_ShouldReturnNull_WhenNoEventsExist() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(List.of());

        // When
        OrderStateDTO result = orderEventService.getOrderCurrentState(orderId);

        // Then
        assertThat(result).isNull();

        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
        verify(orderEventLifecycleTransformer, never()).projectOrderState(any());
    }

    @Test
    void deleteOrder_ShouldCreateDeleteEvent_WhenOrderExists() {
        // Given
        List<OrderEventDocument> existingEvents = List.of(orderEventDocument);
        CreateOrderEventRequest deleteRequest = CreateOrderEventRequest.builder()
                .orderId(orderId)
                .eventType(OrderEventType.ORDER_DELETED)
                .eventSource(eventSource)
                .build();

        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(existingEvents);
        when(orderEventLifecycleTransformer.createDeleteEventRequest(orderId, eventSource))
                .thenReturn(deleteRequest);
        when(orderEventRepository.getNextVersionForOrder(orderId)).thenReturn(2);
        when(orderEventLifecycleTransformer.toEventEntity(deleteRequest, 2))
                .thenReturn(orderEventDocument);
        when(orderEventRepository.save(orderEventDocument)).thenReturn(orderEventDocument);
        when(orderEventLifecycleTransformer.toEventDocumentDTO(orderEventDocument))
                .thenReturn(orderEventDocumentDTO);

        // When
        OrderEventDocumentDTO result = orderEventService.deleteOrder(orderId, eventSource);

        // Then
        assertThat(result).isNotNull();

        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
        verify(orderEventLifecycleTransformer).createDeleteEventRequest(orderId, eventSource);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderDoesNotExist() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> orderEventService.deleteOrder(orderId, eventSource))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found with id: " + orderId);

        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
        verify(orderEventLifecycleTransformer, never()).createDeleteEventRequest(any(), any());
    }

    @Test
    void orderExists_ShouldReturnTrue_WhenEventsExist() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId))
                .thenReturn(List.of(orderEventDocument));

        // When
        boolean result = orderEventService.orderExists(orderId);

        // Then
        assertThat(result).isTrue();
        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
    }

    @Test
    void orderExists_ShouldReturnFalse_WhenNoEventsExist() {
        // Given
        when(orderEventRepository.findByOrderIdOrderedByVersion(orderId)).thenReturn(List.of());

        // When
        boolean result = orderEventService.orderExists(orderId);

        // Then
        assertThat(result).isFalse();
        verify(orderEventRepository).findByOrderIdOrderedByVersion(orderId);
    }
}
