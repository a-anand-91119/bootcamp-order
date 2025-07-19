package dev.notyouraverage.bootcamp_order.kafka_basics.configurations;

import dev.notyouraverage.bootcamp_order.kafka_basics.constants.KafkaConstants;
import dev.notyouraverage.bootcamp_order.kafka_basics.utils.KafkaConfigurationUtils;
import dev.notyouraverage.messages.JsonSerializable;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean(KafkaConstants.JSON_SERIALIZABLE_PRODUCER_FACTORY)
    public ProducerFactory<String, JsonSerializable> jsonSerializableProducerFactory() {
        Map<String, Object> props = KafkaConfigurationUtils.buildCommonProducerConfigs(kafkaProperties);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(KafkaConstants.JSON_SERIALIZABLE_KAFKA_TEMPLATE)
    public KafkaTemplate<String, JsonSerializable> jsonSerializableKafkaTemplate(
            @Qualifier(KafkaConstants.JSON_SERIALIZABLE_PRODUCER_FACTORY) ProducerFactory<String, JsonSerializable> jsonSerializableProducerFactory
    ) {
        KafkaTemplate<String, JsonSerializable> kafkaTemplate = new KafkaTemplate<>(jsonSerializableProducerFactory);
        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate
                .setObservationConvention(new KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention());
        return kafkaTemplate;
    }

    @Bean(KafkaConstants.JSON_SERIALIZABLE_CONSUMER_FACTORY)
    public ConsumerFactory<String, JsonSerializable> jsonSerializableConsumerFactory() {
        Map<String, Object> props = KafkaConfigurationUtils.buildCommonConsumerConfigs(kafkaProperties);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, KafkaConstants.TRUSTED_PACKAGES);

        ErrorHandlingDeserializer<String> keyErrorHandlingDeserializer = new ErrorHandlingDeserializer<>();
        ErrorHandlingDeserializer<JsonSerializable> valueErrorHandlingDeserializer = new ErrorHandlingDeserializer<>();
        return new DefaultKafkaConsumerFactory<>(props, keyErrorHandlingDeserializer, valueErrorHandlingDeserializer);
    }

    @Bean(KafkaConstants.JSON_SERIALIZABLE_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, JsonSerializable> jsonSerializableConcurrentKafkaListenerContainerFactory(
            @Qualifier(KafkaConstants.JSON_SERIALIZABLE_CONSUMER_FACTORY) ConsumerFactory<String, JsonSerializable> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, JsonSerializable> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
