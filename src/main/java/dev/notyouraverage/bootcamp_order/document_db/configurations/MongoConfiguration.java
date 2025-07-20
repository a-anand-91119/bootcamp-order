package dev.notyouraverage.bootcamp_order.document_db.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "dev.notyouraverage.bootcamp_order.document_db.repositories.mongo")
public class MongoConfiguration {
}
