package dev.notyouraverage.bootcamp_order.combine_together.dtos.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class CreateOrderLineItemRequest {

    @NotBlank(message = "Product ID is required") private String productId;

    @NotBlank(message = "Product name is required") private String productName;

    @NotBlank(message = "Product SKU is required") private String productSku;

    @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") private Integer quantity;

    @NotNull(message = "Unit price is required") @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0") private BigDecimal unitPrice;

    @NotNull(message = "Total price is required") @DecimalMin(value = "0.0", message = "Total price must be greater than or equal to 0") private BigDecimal totalPrice;

    @DecimalMin(value = "0.0", message = "Discount amount must be greater than or equal to 0") private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", message = "Tax amount must be greater than or equal to 0") private BigDecimal taxAmount;

    private String category;

    private String description;

    private String imageUrl;

    private Map<String, Object> productAttributes;

    private Map<String, Object> metadata;
}
