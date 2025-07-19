package dev.notyouraverage.bootcamp_order.kafka_basics.configurations;

import dev.notyouraverage.bootcamp_order.kafka_basics.properties.KafkaTopicProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;

@Slf4j
@Configuration
@Profile("local")
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaTopicProperties.class)
public class KafkaTopicConfiguration {

    private final KafkaTopicProperties kafkaTopicProperties;

    @Bean
    public KafkaAdmin.NewTopics dynamicTopicsFromProperties() {
        if (kafkaTopicProperties.getTopics() == null || kafkaTopicProperties.getTopics().isEmpty()) {
            return new KafkaAdmin.NewTopics();
        }

        List<NewTopic> newTopics = new ArrayList<>();
        for (KafkaTopicProperties.TopicConfig topicConfig : kafkaTopicProperties.getTopics()) {
            log.info(
                    "Creating topic: {} with {} partitions and replication factor {}",
                    topicConfig.getName(),
                    topicConfig.getPartitions(),
                    topicConfig.getReplicationFactor()
            );

            NewTopic newTopic = new NewTopic(
                    topicConfig.getName(),
                    topicConfig.getPartitions(),
                    topicConfig.getReplicationFactor()
            );

            if (topicConfig.getConfigs() != null && !topicConfig.getConfigs().isEmpty()) {
                newTopic.configs(topicConfig.getConfigs());
                log.info("Topic {} configured with: {}", topicConfig.getName(), topicConfig.getConfigs());
            }
            newTopics.add(newTopic);
        }
        log.info("Creating {} Kafka topics from properties", newTopics.size());
        return new KafkaAdmin.NewTopics(newTopics.toArray(new NewTopic[0]));
    }
}
