package dev.notyouraverage.bootcamp_order.kickoff.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {

    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters") private String firstName;

    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters") private String lastName;

    @Email(message = "Email must be valid") @Size(max = 100, message = "Email must not exceed 100 characters") private String email;
}
