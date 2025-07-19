package dev.notyouraverage.bootcamp_order.kickoff.dtos;

import java.util.UUID;
import lombok.*;

@Data
@Builder
public class UserDTO {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;
}
