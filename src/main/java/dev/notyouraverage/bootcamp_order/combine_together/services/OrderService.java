package dev.notyouraverage.bootcamp_order.combine_together.services;

import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequest createOrderRequest);

    OrderDTO getOrderById(UUID id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByCustomerId(UUID customerId);
}
