package dev.notyouraverage.bootcamp_order.kafka_basics.properties;

import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.kafka.admin")
public class KafkaTopicProperties {

    private List<TopicConfig> topics;

    @Data
    public static class TopicConfig {
        private String name;

        private Integer partitions = 1;

        private Short replicationFactor = 1;

        private Map<String, String> configs;
    }
}
