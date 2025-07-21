package dev.notyouraverage.bootcamp_order.document_db.dtos.request;

import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderEventRequest {
    @NotBlank private String orderId;

    @NotNull private OrderEventType eventType;

    @NotNull private Map<String, Object> payload;

    @NotNull private LocalDateTime timestamp;

    @NotBlank private String eventSource;
}
