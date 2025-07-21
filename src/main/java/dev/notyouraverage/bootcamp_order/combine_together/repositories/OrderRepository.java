package dev.notyouraverage.bootcamp_order.combine_together.repositories;

import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId")
    List<Order> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.lineItems WHERE o.id = :id")
    Optional<Order> findByIdWithLineItems(@Param("id") UUID id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.lineItems")
    List<Order> findAllWithLineItems();
}
