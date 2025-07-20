package dev.notyouraverage.bootcamp_order.kafka_avro.properties;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.kafka.avro")
@EqualsAndHashCode(callSuper = true)
@Qualifier(KafkaConstants.KAFKA_AVRO_FORMAT_PROPERTIES) public class KafkaAvroFormatProperties extends KafkaProperties {
}
