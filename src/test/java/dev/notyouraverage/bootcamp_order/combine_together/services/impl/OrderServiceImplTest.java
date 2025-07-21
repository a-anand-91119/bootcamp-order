package dev.notyouraverage.bootcamp_order.combine_together.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.notyouraverage.base.exceptions.RestException;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderLineItemRequest;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderChannel;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderSource;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import dev.notyouraverage.bootcamp_order.combine_together.repositories.OrderLineItemRepository;
import dev.notyouraverage.bootcamp_order.combine_together.repositories.OrderRepository;
import dev.notyouraverage.bootcamp_order.combine_together.transformers.OrderAvroEventTransformer;
import dev.notyouraverage.bootcamp_order.combine_together.transformers.OrderTransformer;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.specific.SpecificRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTransformer orderTransformer;

    @Mock
    private OrderAvroEventTransformer orderAvroEventTransformer;

    @Mock
    private KafkaTemplate<String, SpecificRecord> avroKafkaTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CreateOrderRequest createOrderRequest;

    private Order order;

    private OrderDTO orderDTO;

    private OrderCreatedEvent orderCreatedEvent;

    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(UUID.randomUUID());
        createOrderRequest.setCustomerEmail("test@example.com");
        createOrderRequest.setTotalAmount(new BigDecimal("100.00"));
        createOrderRequest.setCurrency("USD");
        createOrderRequest.setSource(OrderSource.WEB);
        createOrderRequest.setChannel(OrderChannel.ONLINE);
        createOrderRequest.setLineItems(List.of()); // Empty line items for basic test

        order = Order.builder()
                .id(orderId)
                .customerId(createOrderRequest.getCustomerId())
                .customerEmail(createOrderRequest.getCustomerEmail())
                .status(OrderStatus.CREATED)
                .totalAmount(createOrderRequest.getTotalAmount())
                .currency(createOrderRequest.getCurrency())
                .source(createOrderRequest.getSource())
                .channel(createOrderRequest.getChannel())
                .orderDate(LocalDateTime.now())
                .build();

        orderDTO = OrderDTO.builder()
                .id(orderId)
                .customerId(createOrderRequest.getCustomerId())
                .customerEmail(createOrderRequest.getCustomerEmail())
                .status(OrderStatus.CREATED)
                .totalAmount(createOrderRequest.getTotalAmount())
                .currency(createOrderRequest.getCurrency())
                .source(createOrderRequest.getSource())
                .channel(createOrderRequest.getChannel())
                .build();

        orderCreatedEvent = OrderCreatedEvent.newBuilder()
                .setOrderId(orderId.toString())
                .setCustomerId(createOrderRequest.getCustomerId().toString())
                .setCustomerEmail(createOrderRequest.getCustomerEmail())
                .setStatus(OrderStatus.CREATED.name())
                .setOrderDate(java.time.Instant.now())
                .setTotalAmount(createOrderRequest.getTotalAmount().toString())
                .setCurrency(createOrderRequest.getCurrency())
                .setLineItems(java.util.List.of())
                .setSource(createOrderRequest.getSource().name())
                .setChannel(createOrderRequest.getChannel().name())
                .setEventTimestamp(java.time.Instant.now())
                .setEventSource("order-service")
                .setEventVersion(1)
                .build();
    }

    @Test
    void createOrder_ShouldCreateOrderAndPublishEvent_WhenValidRequest() {
        // Given
        when(orderTransformer.toEntity(createOrderRequest)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderAvroEventTransformer.toOrderCreatedEvent(order)).thenReturn(orderCreatedEvent);
        when(avroKafkaTemplate.send(isNull(), anyString(), any(SpecificRecord.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        when(orderTransformer.toDTO(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.createOrder(createOrderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        assertThat(result.getCustomerEmail()).isEqualTo("test@example.com");

        verify(orderRepository).save(order);
        verify(orderAvroEventTransformer).toOrderCreatedEvent(order);
        verify(avroKafkaTemplate).send(isNull(), eq(orderId.toString()), any(SpecificRecord.class));
        verify(orderTransformer).toDTO(order);
    }

    @Test
    void createOrder_ShouldCreateOrderWithLineItems_WhenLineItemsProvided() {
        // Given
        CreateOrderLineItemRequest lineItemRequest = new CreateOrderLineItemRequest();
        lineItemRequest.setProductId("PROD-001");
        lineItemRequest.setProductName("Test Product");
        lineItemRequest.setQuantity(2);
        lineItemRequest.setUnitPrice(new BigDecimal("50.00"));
        lineItemRequest.setTotalPrice(new BigDecimal("100.00"));

        createOrderRequest.setLineItems(List.of(lineItemRequest));

        OrderLineItem lineItem = OrderLineItem.builder()
                .id(UUID.randomUUID())
                .productId("PROD-001")
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .totalPrice(new BigDecimal("100.00"))
                .order(order)
                .build();

        List<OrderLineItem> lineItems = List.of(lineItem);
        order.setLineItems(lineItems);

        when(orderTransformer.toEntity(createOrderRequest)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderTransformer.toLineItemEntity(any(), eq(order))).thenReturn(lineItem);
        when(orderLineItemRepository.saveAll(anyList())).thenReturn(lineItems);
        when(orderAvroEventTransformer.toOrderCreatedEvent(order)).thenReturn(orderCreatedEvent);
        when(avroKafkaTemplate.send(isNull(), anyString(), any(SpecificRecord.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        when(orderTransformer.toDTO(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.createOrder(createOrderRequest);

        // Then
        assertThat(result).isNotNull();
        verify(orderLineItemRepository).saveAll(anyList());
        verify(orderTransformer).toLineItemEntity(any(), eq(order));
    }

    @Test
    void getOrderById_ShouldReturnOrderDTO_WhenOrderExists() {
        // Given
        when(orderRepository.findByIdWithLineItems(orderId)).thenReturn(Optional.of(order));
        when(orderTransformer.toDTO(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.getOrderById(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        verify(orderRepository).findByIdWithLineItems(orderId);
        verify(orderTransformer).toDTO(order);
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderNotFound() {
        // Given
        when(orderRepository.findByIdWithLineItems(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(RestException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.NOT_FOUND);

        verify(orderRepository).findByIdWithLineItems(orderId);
        verify(orderTransformer, never()).toDTO(any());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders_WhenOrdersExist() {
        // Given
        List<Order> orders = List.of(order);
        List<OrderDTO> orderDTOs = List.of(orderDTO);

        when(orderRepository.findAllWithLineItems()).thenReturn(orders);
        when(orderTransformer.toDTOs(orders)).thenReturn(orderDTOs);

        // When
        List<OrderDTO> result = orderService.getAllOrders();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(orderId);
        verify(orderRepository).findAllWithLineItems();
        verify(orderTransformer).toDTOs(orders);
    }

    @Test
    void getOrdersByCustomerId_ShouldReturnCustomerOrders_WhenOrdersExist() {
        // Given
        UUID customerId = UUID.randomUUID();
        List<Order> orders = List.of(order);
        List<OrderDTO> orderDTOs = List.of(orderDTO);

        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);
        when(orderTransformer.toDTOs(orders)).thenReturn(orderDTOs);

        // When
        List<OrderDTO> result = orderService.getOrdersByCustomerId(customerId);

        // Then
        assertThat(result).hasSize(1);
        verify(orderRepository).findByCustomerId(customerId);
        verify(orderTransformer).toDTOs(orders);
    }
}
