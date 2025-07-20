package dev.notyouraverage.bootcamp_order.kafka_basics.properties;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.kafka.json")
@EqualsAndHashCode(callSuper = true)
@Primary
@Qualifier(KafkaConstants.KAFKA_JSON_FORMAT_PROPERTIES) public class KafkaJsonFormatProperties extends KafkaProperties {
}
