package dev.notyouraverage.bootcamp_order.combine_together.services.impl;

import dev.notyouraverage.base.dtos.response.wrapper.ErrorResponse;
import dev.notyouraverage.base.exceptions.RestException;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.Order;
import dev.notyouraverage.bootcamp_order.combine_together.models.postgres.OrderLineItem;
import dev.notyouraverage.bootcamp_order.combine_together.repositories.OrderLineItemRepository;
import dev.notyouraverage.bootcamp_order.combine_together.repositories.OrderRepository;
import dev.notyouraverage.bootcamp_order.combine_together.services.OrderService;
import dev.notyouraverage.bootcamp_order.combine_together.transformers.OrderAvroEventTransformer;
import dev.notyouraverage.bootcamp_order.combine_together.transformers.OrderTransformer;
import dev.notyouraverage.bootcamp_order.enums.AppErrorCode;
import dev.notyouraverage.commons.utils.CompletableFutureUtils;
import dev.notyouraverage.messages.avro.OrderCreatedEvent;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderLineItemRepository orderLineItemRepository;

    private final OrderTransformer orderTransformer;

    private final OrderAvroEventTransformer orderAvroEventTransformer;

    private final KafkaTemplate<String, SpecificRecord> avroKafkaTemplate;

    private final String orderAvroTopic;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTransformer orderTransformer,
            OrderAvroEventTransformer orderAvroEventTransformer,
            KafkaTemplate<String, SpecificRecord> avroKafkaTemplate,
            @Value("${app.kafka.topics.order_avro_events}") String orderAvroTopic
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTransformer = orderTransformer;
        this.orderAvroEventTransformer = orderAvroEventTransformer;
        this.avroKafkaTemplate = avroKafkaTemplate;
        this.orderAvroTopic = orderAvroTopic;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest createOrderRequest) {
        Order savedOrder = orderRepository.save(orderTransformer.toEntity(createOrderRequest));

        if (createOrderRequest.getLineItems() != null && !createOrderRequest.getLineItems().isEmpty()) {
            List<OrderLineItem> lineItems = createOrderRequest.getLineItems()
                    .stream()
                    .map(lineItemRequest -> orderTransformer.toLineItemEntity(lineItemRequest, savedOrder))
                    .toList();

            List<OrderLineItem> savedLineItems = orderLineItemRepository.saveAll(lineItems);
            savedOrder.setLineItems(savedLineItems);
        }
        OrderCreatedEvent orderCreatedEvent = orderAvroEventTransformer.toOrderCreatedEvent(savedOrder);
        CompletableFutureUtils.unchekedGet(
                avroKafkaTemplate.send(orderAvroTopic, savedOrder.getId().toString(), orderCreatedEvent)
        );
        log.info("Successfully published OrderCreatedEvent for orderId: {}", savedOrder.getId());
        return orderTransformer.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(UUID id) {
        return orderRepository.findByIdWithLineItems(id)
                .map(orderTransformer::toDTO)
                .orElseThrow(
                        () -> new RestException(HttpStatus.NOT_FOUND, ErrorResponse.from(AppErrorCode.ORDER_NOT_FOUND))
                );
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderTransformer.toDTOs(orderRepository.findAllWithLineItems());
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(UUID customerId) {
        return orderTransformer.toDTOs(orderRepository.findByCustomerId(customerId));
    }
}
