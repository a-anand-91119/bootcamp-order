package dev.notyouraverage.bootcamp_order.combine_together.repositories;

import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, UUID> {
}
