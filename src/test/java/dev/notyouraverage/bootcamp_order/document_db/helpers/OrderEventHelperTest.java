package dev.notyouraverage.bootcamp_order.document_db.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import dev.notyouraverage.messages.avro.OrderLineItem;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderEventHelperTest {

    @Mock
    private OrderEventRepository orderEventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderEventHelper orderEventHelper;

    private OrderCreatedEvent orderCreatedEvent;

    private OrderEventDocument savedOrderEventDocument;

    private Instant eventTimestamp;

    @BeforeEach
    void setUp() {
        eventTimestamp = Instant.now();

        OrderLineItem lineItem = OrderLineItem.newBuilder()
                .setProductId("PROD-001")
                .setProductName("Test Product")
                .setProductSku("SKU-001")
                .setQuantity(2)
                .setUnitPrice("50.00")
                .setTotalPrice("100.00")
                .build();

        orderCreatedEvent = OrderCreatedEvent.newBuilder()
                .setOrderId("order-123")
                .setCustomerId("customer-456")
                .setCustomerEmail("test@example.com")
                .setStatus("CREATED")
                .setOrderDate(eventTimestamp)
                .setTotalAmount("100.00")
                .setCurrency("USD")
                .setLineItems(List.of(lineItem))
                .setShippingAddress("{\"street\": \"123 Main St\"}")
                .setSource("WEB")
                .setChannel("ONLINE")
                .setEventTimestamp(eventTimestamp)
                .setEventSource("order-service")
                .setEventVersion(1)
                .build();

        savedOrderEventDocument = OrderEventDocument.builder()
                .mongoId("mongo-id-123")
                .orderId("order-123")
                .eventType(OrderEventType.ORDER_CREATED)
                .eventSource("order-service")
                .eventVersion(1)
                .timestamp(LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault()))
                .build();
    }

    @Test
    void processOrderCreatedEvent_ShouldSaveEventDocument_WhenValidEvent() throws Exception {
        // Given
        Map<String, Object> expectedPayload = Map.of(
                "orderId",
                "order-123",
                "customerId",
                "customer-456",
                "totalAmount",
                "100.00"
        );

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(expectedPayload);
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(savedOrderEventDocument);

        // When
        orderEventHelper.processOrderCreatedEvent(orderCreatedEvent);

        // Then
        ArgumentCaptor<OrderEventDocument> documentCaptor = ArgumentCaptor.forClass(OrderEventDocument.class);
        verify(orderEventRepository).save(documentCaptor.capture());

        OrderEventDocument capturedDocument = documentCaptor.getValue();
        assertThat(capturedDocument.getOrderId()).isEqualTo("order-123");
        assertThat(capturedDocument.getEventType()).isEqualTo(OrderEventType.ORDER_CREATED);
        assertThat(capturedDocument.getEventSource()).isEqualTo("order-service");
        assertThat(capturedDocument.getEventVersion()).isEqualTo(1);
        LocalDateTime expectedTimestamp = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
        assertThat(capturedDocument.getTimestamp()).isEqualToIgnoringNanos(expectedTimestamp);

        verify(objectMapper).readValue(anyString(), eq(Map.class));
    }

    @Test
    void processOrderCreatedEvent_ShouldUseManualConversion_WhenJsonParsingFails() throws Exception {
        // Given
        when(objectMapper.readValue(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("JSON parsing failed"));
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(savedOrderEventDocument);

        // When
        orderEventHelper.processOrderCreatedEvent(orderCreatedEvent);

        // Then
        ArgumentCaptor<OrderEventDocument> documentCaptor = ArgumentCaptor.forClass(OrderEventDocument.class);
        verify(orderEventRepository).save(documentCaptor.capture());

        OrderEventDocument capturedDocument = documentCaptor.getValue();
        assertThat(capturedDocument.getOrderId()).isEqualTo("order-123");
        assertThat(capturedDocument.getPayload()).containsKey("orderId");
        assertThat(capturedDocument.getPayload()).containsKey("customerId");
        assertThat(capturedDocument.getPayload()).containsKey("totalAmount");
        assertThat(capturedDocument.getPayload()).containsKey("lineItemCount");
        assertThat(capturedDocument.getPayload().get("lineItemCount")).isEqualTo(1);

        verify(objectMapper).readValue(anyString(), eq(Map.class));
    }

    @Test
    void processOrderCreatedEvent_ShouldHandleNullOptionalFields_InManualConversion() throws Exception {
        // Given
        OrderCreatedEvent eventWithNulls = OrderCreatedEvent.newBuilder(orderCreatedEvent)
                .setCustomerEmail(null)
                .setChannel(null)
                .setShippingAddress(null)
                .build();

        when(objectMapper.readValue(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("JSON parsing failed"));
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(savedOrderEventDocument);

        // When
        orderEventHelper.processOrderCreatedEvent(eventWithNulls);

        // Then
        ArgumentCaptor<OrderEventDocument> documentCaptor = ArgumentCaptor.forClass(OrderEventDocument.class);
        verify(orderEventRepository).save(documentCaptor.capture());

        OrderEventDocument capturedDocument = documentCaptor.getValue();
        Map<String, Object> payload = capturedDocument.getPayload();

        assertThat(payload.get("customerEmail")).isNull();
        assertThat(payload.get("channel")).isNull();
        assertThat(payload.get("shippingAddress")).isNull();
        assertThat(payload.get("orderId")).isEqualTo("order-123");
    }

    @Test
    void processOrderCreatedEvent_ShouldRethrowException_WhenRepositorySaveFails() throws Exception {
        // Given
        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(Map.of());
        when(orderEventRepository.save(any(OrderEventDocument.class)))
                .thenThrow(new RuntimeException("Database save failed"));

        // When & Then
        assertThatThrownBy(() -> orderEventHelper.processOrderCreatedEvent(orderCreatedEvent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database save failed");

        verify(orderEventRepository).save(any(OrderEventDocument.class));
    }

    @Test
    void processOrderCreatedEvent_ShouldConvertTimestamp_Correctly() throws Exception {
        // Given
        Instant specificTimestamp = Instant.parse("2023-12-25T10:30:00Z");
        OrderCreatedEvent eventWithSpecificTimestamp = OrderCreatedEvent.newBuilder(orderCreatedEvent)
                .setEventTimestamp(specificTimestamp)
                .build();

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(Map.of());
        when(orderEventRepository.save(any(OrderEventDocument.class))).thenReturn(savedOrderEventDocument);

        // When
        orderEventHelper.processOrderCreatedEvent(eventWithSpecificTimestamp);

        // Then
        ArgumentCaptor<OrderEventDocument> documentCaptor = ArgumentCaptor.forClass(OrderEventDocument.class);
        verify(orderEventRepository).save(documentCaptor.capture());

        OrderEventDocument capturedDocument = documentCaptor.getValue();
        LocalDateTime expectedDateTime = LocalDateTime.ofInstant(specificTimestamp, ZoneId.systemDefault());
        assertThat(capturedDocument.getTimestamp()).isEqualTo(expectedDateTime);
    }
}
