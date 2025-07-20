package dev.notyouraverage.bootcamp_order.document_db.dtos;

import dev.notyouraverage.bootcamp_order.document_db.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStateDTO {
    private String orderId;

    private OrderStatus currentStatus;

    private String customerId;

    private LocalDateTime orderCreatedAt;

    private LocalDateTime lastUpdatedAt;

    private Integer currentEventVersion;

    private Map<String, Object> currentPayload;

    private List<OrderEventDocumentDTO> events;

    private boolean isDeleted;
}
