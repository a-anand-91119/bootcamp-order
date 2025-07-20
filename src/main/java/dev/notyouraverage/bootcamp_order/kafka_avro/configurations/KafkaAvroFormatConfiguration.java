package dev.notyouraverage.bootcamp_order.kafka_avro.configurations;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import dev.notyouraverage.bootcamp_order.kafka_avro.properties.KafkaAvroFormatProperties;
import dev.notyouraverage.bootcamp_order.kafka_basics.utils.KafkaConfigurationUtils;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation;

@Configuration
@RequiredArgsConstructor
public class KafkaAvroFormatConfiguration {

    private final KafkaAvroFormatProperties kafkaAvroFormatProperties;

    @Bean(KafkaConstants.AVRO_PRODUCER_FACTORY)
    public ProducerFactory<String, SpecificRecord> avroProducerFactory() {
        Map<String, Object> props = KafkaConfigurationUtils.buildCommonProducerConfigs(kafkaAvroFormatProperties);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(KafkaConstants.AVRO_KAFKA_TEMPLATE)
    public KafkaTemplate<String, SpecificRecord> avroKafkaTemplate(
            @Qualifier(KafkaConstants.AVRO_PRODUCER_FACTORY) ProducerFactory<String, SpecificRecord> avroProducerFactory
    ) {
        KafkaTemplate<String, SpecificRecord> kafkaTemplate = new KafkaTemplate<>(avroProducerFactory);
        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate
                .setObservationConvention(new KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention());
        return kafkaTemplate;
    }

    @Bean(KafkaConstants.AVRO_CONSUMER_FACTORY)
    public ConsumerFactory<String, SpecificRecord> avroConsumerFactory() {
        Map<String, Object> props = KafkaConfigurationUtils.buildCommonConsumerConfigs(kafkaAvroFormatProperties);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(KafkaConstants.AVRO_CONCURRENT_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecord> avroConcurrentKafkaListenerContainerFactory(
            @Qualifier(KafkaConstants.AVRO_CONSUMER_FACTORY) ConsumerFactory<String, SpecificRecord> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, SpecificRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
