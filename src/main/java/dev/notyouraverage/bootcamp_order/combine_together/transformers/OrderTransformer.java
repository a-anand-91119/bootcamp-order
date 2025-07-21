package dev.notyouraverage.bootcamp_order.combine_together.transformers;

import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderLineItemDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderLineItemRequest;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import java.util.List;

@Transformer
public class OrderTransformer {

    public Order toEntity(CreateOrderRequest request) {
        return Order.builder()
                .customerId(request.getCustomerId())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .status(OrderStatus.CREATED)
                .orderDate(request.getOrderDate())
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .subtotal(request.getSubtotal())
                .taxAmount(request.getTaxAmount())
                .shippingAmount(request.getShippingAmount())
                .discountAmount(request.getDiscountAmount())
                .totalAmount(request.getTotalAmount())
                .currency(request.getCurrency())
                .shippingMethod(request.getShippingMethod())
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(request.getPaymentStatus())
                .paymentTransactionId(request.getPaymentTransactionId())
                .notes(request.getNotes())
                .source(request.getSource())
                .channel(request.getChannel())
                .promotions(request.getPromotions())
                .metadata(request.getMetadata())
                .build();
    }

    public OrderLineItem toLineItemEntity(CreateOrderLineItemRequest request, Order order) {
        return OrderLineItem.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .productSku(request.getProductSku())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getTotalPrice())
                .discountAmount(request.getDiscountAmount())
                .taxAmount(request.getTaxAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .productAttributes(request.getProductAttributes())
                .metadata(request.getMetadata())
                .order(order)
                .build();
    }

    public OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .actualDeliveryDate(order.getActualDeliveryDate())
                .subtotal(order.getSubtotal())
                .taxAmount(order.getTaxAmount())
                .shippingAmount(order.getShippingAmount())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .shippingMethod(order.getShippingMethod())
                .trackingNumber(order.getTrackingNumber())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .paymentTransactionId(order.getPaymentTransactionId())
                .lineItems(order.getLineItems() != null ? toLineItemDTOs(order.getLineItems()) : List.of())
                .notes(order.getNotes())
                .source(order.getSource())
                .channel(order.getChannel())
                .promotions(order.getPromotions())
                .metadata(order.getMetadata())
                .build();
    }

    public OrderLineItemDTO toLineItemDTO(OrderLineItem lineItem) {
        return OrderLineItemDTO.builder()
                .id(lineItem.getId())
                .productId(lineItem.getProductId())
                .productName(lineItem.getProductName())
                .productSku(lineItem.getProductSku())
                .quantity(lineItem.getQuantity())
                .unitPrice(lineItem.getUnitPrice())
                .totalPrice(lineItem.getTotalPrice())
                .discountAmount(lineItem.getDiscountAmount())
                .taxAmount(lineItem.getTaxAmount())
                .category(lineItem.getCategory())
                .description(lineItem.getDescription())
                .imageUrl(lineItem.getImageUrl())
                .productAttributes(lineItem.getProductAttributes())
                .metadata(lineItem.getMetadata())
                .build();
    }

    public List<OrderLineItemDTO> toLineItemDTOs(List<OrderLineItem> lineItems) {
        return lineItems.stream()
                .map(this::toLineItemDTO)
                .toList();
    }

    public List<OrderDTO> toDTOs(List<Order> orders) {
        return orders.stream()
                .map(this::toDTO)
                .toList();
    }
}
