package dev.notyouraverage.bootcamp_order.kickoff.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "dev.notyouraverage.bootcamp_order.kickoff.repositories")
public class JpaConfigurations {
}
