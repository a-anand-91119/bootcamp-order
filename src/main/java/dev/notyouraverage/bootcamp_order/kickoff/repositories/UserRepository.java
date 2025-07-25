package dev.notyouraverage.bootcamp_order.kickoff.repositories;

import dev.notyouraverage.bootcamp_order.kickoff.models.postgres.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
