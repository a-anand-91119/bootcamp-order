package dev.notyouraverage.bootcamp_order.kafka_basics.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import dev.notyouraverage.bootcamp_order.kafka_basics.dtos.request.CreateOrderRequest;
import dev.notyouraverage.bootcamp_order.kafka_basics.services.KafkaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaService kafkaService;

    @PostMapping("/send")
    public ResponseEntity<ResponseWrapper<String>> send(@Valid @RequestBody CreateOrderRequest request) {
        kafkaService.send(request);
        return ResponseEntity.ok(ResponseWrapper.success("Order created message sent successfully"));
    }
}
