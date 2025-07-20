package dev.notyouraverage.bootcamp_order.document_db.repositories.mongo;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.enums.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventMongoRepository extends MongoRepository<OrderEventDocument, String> {

    Optional<OrderEventDocument> findByIdAndDocumentStatus(String id, Status status);

    List<OrderEventDocument> findByOrderIdAndDocumentStatusOrderByEventVersionAsc(String orderId, Status status);

    List<OrderEventDocument> findByOrderIdAndDocumentStatusOrderByTimestampAsc(String orderId, Status status);

    Optional<OrderEventDocument> findTopByOrderIdAndDocumentStatusOrderByEventVersionDesc(
            String orderId,
            Status status
    );

    List<OrderEventDocument> findByEventTypeAndDocumentStatus(OrderEventType eventType, Status status);

    boolean existsByOrderIdAndEventVersionAndDocumentStatus(String orderId, Integer eventVersion, Status status);

    @Query("{ 'orderId': ?0, 'documentStatus': ?1, 'eventVersion': { $lte: ?2 } }")
    List<OrderEventDocument> findByOrderIdAndDocumentStatusAndEventVersionLessThanEqual(
            String orderId,
            Status status,
            Integer maxVersion
    );

    List<OrderEventDocument> findByDocumentStatusOrderByOrderIdAscEventVersionAsc(Status status);
}
