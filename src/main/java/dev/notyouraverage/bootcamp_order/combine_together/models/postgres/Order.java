package dev.notyouraverage.bootcamp_order.combine_together.models.postgres;

import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderChannel;
import dev.notyouraverage.bootcamp_order.combine_together.enums.OrderSource;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import dev.notyouraverage.bootcamp_order.kickoff.models.postgres.Base;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Order extends Base {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "shipping_amount", precision = 19, scale = 2)
    private BigDecimal shippingAmount;

    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "shipping_address")
    private Map<String, Object> shippingAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "billing_address")
    private Map<String, Object> billingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private OrderSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel")
    private OrderChannel channel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "promotions")
    private Map<String, Object> promotions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderLineItem> lineItems;

}
