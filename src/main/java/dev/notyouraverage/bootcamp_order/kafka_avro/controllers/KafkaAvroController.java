package dev.notyouraverage.bootcamp_order.kafka_avro.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import dev.notyouraverage.bootcamp_order.kafka_avro.services.KafkaAvroService;
import dev.notyouraverage.messages.avro.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka/avro")
@RequiredArgsConstructor
public class KafkaAvroController {

    private final KafkaAvroService kafkaAvroService;

    @PostMapping("send")
    public ResponseEntity<ResponseWrapper<String>> sendUser(@RequestBody UserRequest request) {
        User user = User.newBuilder()
                .setFirstName(request.firstName())
                .setLastName(request.lastName())
                .setPhoneNumber(Integer.parseInt(request.phoneNumber()))
                .build();

        kafkaAvroService.sendUser(user);
        return ResponseEntity.ok(ResponseWrapper.success("User sent via Avro"));
    }

    public record UserRequest(String firstName, String lastName, String phoneNumber) {
    }
}
