package dev.notyouraverage.bootcamp_order.configurations;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = { "dev.notyouraverage.bootcamp_order.kickoff.repositories",
        "dev.notyouraverage.bootcamp_order.combine_together.repositories" })
public class JpaConfigurations {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system");
    }

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}
