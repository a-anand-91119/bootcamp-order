package dev.notyouraverage.bootcamp_order.kickoff.models.postgres;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Base {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

}
