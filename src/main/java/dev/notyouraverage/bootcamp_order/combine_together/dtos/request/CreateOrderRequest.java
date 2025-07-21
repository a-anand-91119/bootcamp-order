package dev.notyouraverage.bootcamp_order.combine_together.dtos.request;

import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderChannel;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull(message = "Customer ID is required") private UUID customerId;

    @NotBlank(message = "Customer email is required") @Email(message = "Customer email must be valid") private String customerEmail;

    private String customerPhone;

    @NotNull(message = "Order date is required") private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    @NotNull(message = "Subtotal is required") @DecimalMin(value = "0.0", message = "Subtotal must be greater than or equal to 0") private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "Tax amount must be greater than or equal to 0") private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Shipping amount must be greater than or equal to 0") private BigDecimal shippingAmount;

    @DecimalMin(value = "0.0", message = "Discount amount must be greater than or equal to 0") private BigDecimal discountAmount;

    @NotNull(message = "Total amount is required") @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0") private BigDecimal totalAmount;

    @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be a valid ISO 4217 currency code") private String currency;

    private String shippingMethod;

    @Valid private Map<String, Object> shippingAddress;

    @Valid private Map<String, Object> billingAddress;

    private String paymentMethod;

    private String paymentStatus;

    private String paymentTransactionId;

    @NotEmpty(message = "Order must have at least one line item") @Valid private List<CreateOrderLineItemRequest> lineItems;

    private String notes;

    @NotNull(message = "Source is required") private OrderSource source;

    private OrderChannel channel;

    private Map<String, Object> promotions;

    private Map<String, Object> metadata;
}
