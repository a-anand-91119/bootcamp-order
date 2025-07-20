package dev.notyouraverage.bootcamp_order.document_db.repositories.impl;

import dev.notyouraverage.bootcamp_order.document_db.documents.OrderEventDocument;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.enums.Status;
import dev.notyouraverage.bootcamp_order.document_db.repositories.OrderEventRepository;
import dev.notyouraverage.bootcamp_order.document_db.repositories.mongo.OrderEventMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderEventRepositoryImpl implements OrderEventRepository {

    private final OrderEventMongoRepository orderEventMongoRepository;

    @Override
    public OrderEventDocument save(OrderEventDocument orderEvent) {
        return orderEventMongoRepository.save(orderEvent);
    }

    @Override
    public Optional<OrderEventDocument> findById(String id) {
        return orderEventMongoRepository.findByIdAndDocumentStatus(id, Status.ACTIVE)
                .map(this::hideMongoId);
    }

    @Override
    public List<OrderEventDocument> findByOrderIdOrderedByVersion(String orderId) {
        return orderEventMongoRepository.findByOrderIdAndDocumentStatusOrderByEventVersionAsc(orderId, Status.ACTIVE)
                .stream()
                .map(this::hideMongoId)
                .toList();
    }

    @Override
    public List<OrderEventDocument> findByOrderIdOrderedByTimestamp(String orderId) {
        return orderEventMongoRepository.findByOrderIdAndDocumentStatusOrderByTimestampAsc(orderId, Status.ACTIVE)
                .stream()
                .map(this::hideMongoId)
                .toList();
    }

    @Override
    public Optional<OrderEventDocument> findLatestEventByOrderId(String orderId) {
        return orderEventMongoRepository
                .findTopByOrderIdAndDocumentStatusOrderByEventVersionDesc(orderId, Status.ACTIVE)
                .map(this::hideMongoId);
    }

    @Override
    public List<OrderEventDocument> findByEventType(OrderEventType eventType) {
        return orderEventMongoRepository.findByEventTypeAndDocumentStatus(eventType, Status.ACTIVE)
                .stream()
                .map(this::hideMongoId)
                .toList();
    }

    @Override
    public boolean existsByOrderIdAndEventVersion(String orderId, Integer eventVersion) {
        return orderEventMongoRepository
                .existsByOrderIdAndEventVersionAndDocumentStatus(orderId, eventVersion, Status.ACTIVE);
    }

    @Override
    public List<OrderEventDocument> findByOrderIdUpToVersion(String orderId, Integer maxVersion) {
        return orderEventMongoRepository
                .findByOrderIdAndDocumentStatusAndEventVersionLessThanEqual(orderId, Status.ACTIVE, maxVersion)
                .stream()
                .map(this::hideMongoId)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        orderEventMongoRepository.findByIdAndDocumentStatus(id, Status.ACTIVE)
                .ifPresent(orderEvent -> {
                    orderEvent.setDocumentStatus(Status.DELETED);
                    orderEventMongoRepository.save(orderEvent);
                });
    }

    @Override
    public Integer getNextVersionForOrder(String orderId) {
        return findLatestEventByOrderId(orderId)
                .map(event -> event.getEventVersion() + 1)
                .orElse(1);
    }

    @Override
    public List<OrderEventDocument> findAllOrderedByOrderIdAndVersion() {
        return orderEventMongoRepository.findByDocumentStatusOrderByOrderIdAscEventVersionAsc(Status.ACTIVE)
                .stream()
                .map(this::hideMongoId)
                .toList();
    }

    private OrderEventDocument hideMongoId(OrderEventDocument orderEvent) {
        return orderEvent.toBuilder()
                .mongoId(null)
                .build();
    }
}
