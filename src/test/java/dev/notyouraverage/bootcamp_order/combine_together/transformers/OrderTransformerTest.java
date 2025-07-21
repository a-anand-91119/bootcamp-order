package dev.notyouraverage.bootcamp_order.combine_together.transformers;

import static org.assertj.core.api.Assertions.assertThat;

import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderLineItemDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderLineItemRequest;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderChannel;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderSource;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTransformerTest {

    @InjectMocks
    private OrderTransformer orderTransformer;

    private CreateOrderRequest createOrderRequest;

    private CreateOrderLineItemRequest createOrderLineItemRequest;

    private Order order;

    private OrderLineItem orderLineItem;

    private UUID customerId;

    private UUID orderId;

    private UUID lineItemId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        lineItemId = UUID.randomUUID();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("street", "123 Main St");
        shippingAddress.put("city", "Springfield");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("campaign", "summer-sale");

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(customerId);
        createOrderRequest.setCustomerEmail("test@example.com");
        createOrderRequest.setCustomerPhone("555-0123");
        createOrderRequest.setOrderDate(LocalDateTime.now());
        createOrderRequest.setSubtotal(new BigDecimal("90.00"));
        createOrderRequest.setTaxAmount(new BigDecimal("7.20"));
        createOrderRequest.setShippingAmount(new BigDecimal("5.99"));
        createOrderRequest.setTotalAmount(new BigDecimal("103.19"));
        createOrderRequest.setCurrency("USD");
        createOrderRequest.setShippingAddress(shippingAddress);
        createOrderRequest.setSource(OrderSource.WEB);
        createOrderRequest.setChannel(OrderChannel.ONLINE);
        createOrderRequest.setMetadata(metadata);

        createOrderLineItemRequest = new CreateOrderLineItemRequest();
        createOrderLineItemRequest.setProductId("PROD-001");
        createOrderLineItemRequest.setProductName("Test Product");
        createOrderLineItemRequest.setProductSku("SKU-001");
        createOrderLineItemRequest.setQuantity(2);
        createOrderLineItemRequest.setUnitPrice(new BigDecimal("45.00"));
        createOrderLineItemRequest.setTotalPrice(new BigDecimal("90.00"));

        order = Order.builder()
                .id(orderId)
                .customerId(customerId)
                .customerEmail("test@example.com")
                .customerPhone("555-0123")
                .status(OrderStatus.CREATED)
                .orderDate(LocalDateTime.now())
                .subtotal(new BigDecimal("90.00"))
                .taxAmount(new BigDecimal("7.20"))
                .shippingAmount(new BigDecimal("5.99"))
                .totalAmount(new BigDecimal("103.19"))
                .currency("USD")
                .shippingAddress(shippingAddress)
                .source(OrderSource.WEB)
                .channel(OrderChannel.ONLINE)
                .metadata(metadata)
                .build();

        orderLineItem = OrderLineItem.builder()
                .id(lineItemId)
                .productId("PROD-001")
                .productName("Test Product")
                .productSku("SKU-001")
                .quantity(2)
                .unitPrice(new BigDecimal("45.00"))
                .totalPrice(new BigDecimal("90.00"))
                .order(order)
                .build();
    }

    @Test
    void toEntity_ShouldTransformRequestToEntity_WhenValidRequest() {
        // When
        Order result = orderTransformer.toEntity(createOrderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(result.getCustomerPhone()).isEqualTo("555-0123");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.getSubtotal()).isEqualTo(new BigDecimal("90.00"));
        assertThat(result.getTaxAmount()).isEqualTo(new BigDecimal("7.20"));
        assertThat(result.getShippingAmount()).isEqualTo(new BigDecimal("5.99"));
        assertThat(result.getTotalAmount()).isEqualTo(new BigDecimal("103.19"));
        assertThat(result.getCurrency()).isEqualTo("USD");
        assertThat(result.getSource()).isEqualTo(OrderSource.WEB);
        assertThat(result.getChannel()).isEqualTo(OrderChannel.ONLINE);
        assertThat(result.getShippingAddress()).containsEntry("street", "123 Main St");
        assertThat(result.getMetadata()).containsEntry("campaign", "summer-sale");
    }

    @Test
    void toLineItemEntity_ShouldTransformRequestToEntity_WhenValidRequest() {
        // When
        OrderLineItem result = orderTransformer.toLineItemEntity(createOrderLineItemRequest, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo("PROD-001");
        assertThat(result.getProductName()).isEqualTo("Test Product");
        assertThat(result.getProductSku()).isEqualTo("SKU-001");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualTo(new BigDecimal("45.00"));
        assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("90.00"));
        assertThat(result.getOrder()).isEqualTo(order);
    }

    @Test
    void toDTO_ShouldTransformEntityToDTO_WhenValidEntity() {
        // Given
        order.setLineItems(List.of(orderLineItem));

        // When
        OrderDTO result = orderTransformer.toDTO(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.getTotalAmount()).isEqualTo(new BigDecimal("103.19"));
        assertThat(result.getSource()).isEqualTo(OrderSource.WEB);
        assertThat(result.getChannel()).isEqualTo(OrderChannel.ONLINE);
        assertThat(result.getLineItems()).hasSize(1);
        assertThat(result.getLineItems().get(0).getProductId()).isEqualTo("PROD-001");
    }

    @Test
    void toDTO_ShouldHandleEmptyLineItems_WhenNoLineItemsPresent() {
        // Given
        order.setLineItems(null);

        // When
        OrderDTO result = orderTransformer.toDTO(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLineItems()).isEmpty();
    }

    @Test
    void toLineItemDTO_ShouldTransformEntityToDTO_WhenValidEntity() {
        // When
        OrderLineItemDTO result = orderTransformer.toLineItemDTO(orderLineItem);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(lineItemId);
        assertThat(result.getProductId()).isEqualTo("PROD-001");
        assertThat(result.getProductName()).isEqualTo("Test Product");
        assertThat(result.getProductSku()).isEqualTo("SKU-001");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualTo(new BigDecimal("45.00"));
        assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("90.00"));
    }

    @Test
    void toLineItemDTOs_ShouldTransformListToDTO_WhenValidList() {
        // Given
        OrderLineItem secondLineItem = OrderLineItem.builder()
                .id(UUID.randomUUID())
                .productId("PROD-002")
                .productName("Another Product")
                .productSku("SKU-002")
                .quantity(1)
                .unitPrice(new BigDecimal("25.00"))
                .totalPrice(new BigDecimal("25.00"))
                .build();

        List<OrderLineItem> lineItems = List.of(orderLineItem, secondLineItem);

        // When
        List<OrderLineItemDTO> result = orderTransformer.toLineItemDTOs(lineItems);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductId()).isEqualTo("PROD-001");
        assertThat(result.get(1).getProductId()).isEqualTo("PROD-002");
    }

    @Test
    void toDTOs_ShouldTransformOrderListToDTOs_WhenValidList() {
        // Given
        Order secondOrder = Order.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .customerEmail("another@example.com")
                .status(OrderStatus.PAID)
                .totalAmount(new BigDecimal("50.00"))
                .currency("USD")
                .source(OrderSource.MOBILE_APP)
                .channel(OrderChannel.ONLINE)
                .build();

        List<Order> orders = List.of(order, secondOrder);

        // When
        List<OrderDTO> result = orderTransformer.toDTOs(orders);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(orderId);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(result.get(1).getSource()).isEqualTo(OrderSource.MOBILE_APP);
    }

    @Test
    void toEntity_ShouldHandleNullOptionalFields_WhenRequestHasNulls() {
        // Given
        CreateOrderRequest requestWithNulls = new CreateOrderRequest();
        requestWithNulls.setCustomerId(customerId);
        requestWithNulls.setCustomerEmail("test@example.com");
        requestWithNulls.setTotalAmount(new BigDecimal("100.00"));
        requestWithNulls.setCurrency("USD");
        requestWithNulls.setSource(OrderSource.WEB);
        // Other fields are null

        // When
        Order result = orderTransformer.toEntity(requestWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getCustomerPhone()).isNull();
        assertThat(result.getShippingAddress()).isNull();
        assertThat(result.getChannel()).isNull();
        assertThat(result.getMetadata()).isNull();
    }
}
