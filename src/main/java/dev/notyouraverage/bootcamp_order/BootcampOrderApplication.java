package dev.notyouraverage.bootcamp_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "dev.notyouraverage.base", "dev.notyouraverage.bootcamp_order" })

public class BootcampOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootcampOrderApplication.class, args);
    }

}
