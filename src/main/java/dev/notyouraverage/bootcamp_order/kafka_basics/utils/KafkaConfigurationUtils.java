package dev.notyouraverage.bootcamp_order.kafka_basics.utils;

import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@UtilityClass
public class KafkaConfigurationUtils {

    public static Map<String, Object> buildCommonProducerConfigs(KafkaProperties kafkaProperties) {
        return kafkaProperties.buildProducerProperties(null);
    }

    public static Map<String, Object> buildCommonConsumerConfigs(KafkaProperties kafkaProperties) {
        return kafkaProperties.buildConsumerProperties(null);
    }
}
