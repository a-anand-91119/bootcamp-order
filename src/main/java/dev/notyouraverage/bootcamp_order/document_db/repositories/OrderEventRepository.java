package dev.notyouraverage.bootcamp_order.document_db.repositories;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import java.util.List;
import java.util.Optional;

public interface OrderEventRepository {
    OrderEventDocument save(OrderEventDocument orderEvent);

    Optional<OrderEventDocument> findById(String id);

    List<OrderEventDocument> findByOrderIdOrderedByVersion(String orderId);

    List<OrderEventDocument> findByOrderIdOrderedByTimestamp(String orderId);

    Optional<OrderEventDocument> findLatestEventByOrderId(String orderId);

    List<OrderEventDocument> findByEventType(OrderEventType eventType);

    boolean existsByOrderIdAndEventVersion(String orderId, Integer eventVersion);

    List<OrderEventDocument> findByOrderIdUpToVersion(String orderId, Integer maxVersion);

    void deleteById(String id);

    Integer getNextVersionForOrder(String orderId);

    List<OrderEventDocument> findAllOrderedByOrderIdAndVersion();
}
