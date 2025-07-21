package dev.notyouraverage.bootcamp_order.combine_together.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.OrderDTO;
import dev.notyouraverage.bootcamp_order.combine_together.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.combine_together.services.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<OrderDTO>> createOrder(
            @RequestBody @Valid CreateOrderRequest createOrderRequest
    ) {
        OrderDTO createdOrder = orderService.createOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(createdOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<OrderDTO>> getOrderById(@PathVariable UUID id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ResponseWrapper.success(order));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<OrderDTO>>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ResponseWrapper.success(orders));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseWrapper<List<OrderDTO>>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(orders));
    }
}
