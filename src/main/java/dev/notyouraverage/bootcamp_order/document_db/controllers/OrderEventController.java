package dev.notyouraverage.bootcamp_order.document_db.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderEventDocumentDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.OrderStateDTO;
import dev.notyouraverage.bootcamp_order.document_db.dtos.request.CreateOrderEventRequest;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import dev.notyouraverage.bootcamp_order.document_db.services.OrderEventService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderEventController {

    private final OrderEventService orderEventService;

    @PostMapping("events")
    public ResponseEntity<ResponseWrapper<OrderEventDocumentDTO>> createOrderEvent(
            @Valid @RequestBody CreateOrderEventRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(orderEventService.createOrderEvent(request)));
    }

    @GetMapping("{orderId}/events")
    public ResponseEntity<ResponseWrapper<List<OrderEventDocumentDTO>>> getEventsByOrderId(
            @PathVariable String orderId
    ) {
        return ResponseEntity.ok(ResponseWrapper.success(orderEventService.getEventsByOrderId(orderId)));
    }

    @GetMapping("{orderId}/events/up-to-version/{version}")
    public ResponseEntity<ResponseWrapper<List<OrderEventDocumentDTO>>> getEventsByOrderIdUpToVersion(
            @PathVariable String orderId,
            @PathVariable Integer version
    ) {
        return ResponseEntity
                .ok(ResponseWrapper.success(orderEventService.getEventsByOrderIdUpToVersion(orderId, version)));
    }

    @GetMapping("events/by-type/{eventType}")
    public ResponseEntity<ResponseWrapper<List<OrderEventDocumentDTO>>> getEventsByType(
            @PathVariable OrderEventType eventType
    ) {
        return ResponseEntity.ok(ResponseWrapper.success(orderEventService.getEventsByType(eventType)));
    }

    @GetMapping("{orderId}/state")
    public ResponseEntity<ResponseWrapper<OrderStateDTO>> getOrderCurrentState(@PathVariable String orderId) {
        OrderStateDTO orderState = orderEventService.getOrderCurrentState(orderId);
        if (orderState == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ResponseWrapper.success(orderState));
    }

    @GetMapping("{orderId}/state/at-version/{version}")
    public ResponseEntity<ResponseWrapper<OrderStateDTO>> getOrderStateAtVersion(
            @PathVariable String orderId,
            @PathVariable Integer version
    ) {
        OrderStateDTO orderState = orderEventService.getOrderStateAtVersion(orderId, version);
        if (orderState == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ResponseWrapper.success(orderState));
    }

    @GetMapping("by-customer/{customerId}/states")
    public ResponseEntity<ResponseWrapper<List<OrderStateDTO>>> getOrderStatesByCustomerId(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(ResponseWrapper.success(orderEventService.getOrderStatesByCustomerId(customerId)));
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<ResponseWrapper<OrderEventDocumentDTO>> deleteOrder(
            @PathVariable String orderId,
            @RequestParam(defaultValue = "order-service") String eventSource
    ) {
        return ResponseEntity.ok(ResponseWrapper.success(orderEventService.deleteOrder(orderId, eventSource)));
    }

    @GetMapping("{orderId}/exists")
    public ResponseEntity<ResponseWrapper<Boolean>> orderExists(@PathVariable String orderId) {
        return ResponseEntity.ok(ResponseWrapper.success(orderEventService.orderExists(orderId)));
    }
}
