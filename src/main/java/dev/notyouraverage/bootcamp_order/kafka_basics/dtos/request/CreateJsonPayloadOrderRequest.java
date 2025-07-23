package dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request;

import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateJsonPayloadOrderRequest {

    @NotBlank(message = "Order message is required") @Size(min = 1, max = 500, message = "Order message must be between 1 and 500 characters") private String message;

    @NotBlank private String customerId;

    @NotBlank private String orderId;

    @NotNull private OrderEventType eventType;

    private String eventSource;

    private Integer eventVersion;

    @NotNull private LocalDateTime timestamp;

    @NotNull private Map<String, Object> payload;

}
