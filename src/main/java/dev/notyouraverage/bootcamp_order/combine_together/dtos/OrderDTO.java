package dev.notyouraverage.bootcamp_order.combine_together.dtos;

import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderChannel;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderSource;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
    private UUID id;

    private UUID customerId;

    private String customerEmail;

    private String customerPhone;

    private OrderStatus status;

    private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    private LocalDateTime actualDeliveryDate;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal shippingAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String currency;

    private String shippingMethod;

    private String trackingNumber;

    private Map<String, Object> shippingAddress;

    private Map<String, Object> billingAddress;

    private String paymentMethod;

    private String paymentStatus;

    private String paymentTransactionId;

    private List<OrderLineItemDTO> lineItems;

    private String notes;

    private OrderSource source;

    private OrderChannel channel;

    private Map<String, Object> promotions;

    private Map<String, Object> metadata;

}
