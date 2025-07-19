package dev.notyouraverage.bootcamp_order.kickoff.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello World!";
    }
}
