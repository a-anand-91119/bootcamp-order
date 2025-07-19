package dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "Order message is required") @Size(min = 1, max = 500, message = "Order message must be between 1 and 500 characters") private String message;
}
