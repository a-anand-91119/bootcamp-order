package dev.notyouraverage.bootcamp_order.combine_together.dtos;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineItemDTO {
    private UUID id;

    // Product information
    private String productId;

    private String productName;

    private String productSku;

    private Integer quantity;

    // Financial information
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    // Product details
    private String category;

    private String description;

    private String imageUrl;

    private Map<String, Object> productAttributes; // size, color, variant, etc.

    private Map<String, Object> metadata; // additional data

}
