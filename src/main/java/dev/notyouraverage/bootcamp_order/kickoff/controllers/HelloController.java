package dev.notyouraverage.bootcamp_order.kickoff.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<ResponseWrapper<String>> hello() {
        return ResponseEntity.ok(ResponseWrapper.success("Hello World"));
    }
}
