package dev.notyouraverage.bootcamp_order.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {
    public static final String JSON_SERIALIZABLE_PRODUCER_FACTORY = "jsonSerializableProducerFactory";

    public static final String JSON_SERIALIZABLE_KAFKA_TEMPLATE = "jsonSerializableKafkaTemplate";

    public static final String JSON_SERIALIZABLE_CONSUMER_FACTORY = "jsonSerializableConsumerFactory";

    public static final String JSON_SERIALIZABLE_CONCURRENT_LISTENER_CONTAINER_FACTORY = "jsonSerializableConcurrentKafkaListenerContainerFactory";

    public static final String TRUSTED_PACKAGES = "dev.notyouraverage.messages.*";

    public static final String ORDER_CREATED_LISTENER_ID = "OrderCreatedListenerId";

    public static final String ORDER_CREATED_LISTENER_GROUP = "OrderCreatedListenerGroup";

    public static final String KAFKA_JSON_FORMAT_PROPERTIES = "kafkaJsonFormatProperties";

    public static final String KAFKA_AVRO_FORMAT_PROPERTIES = "kafkaAvroFormatProperties";

    public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

    public static final String AVRO_KAFKA_TEMPLATE = "avroKafkaTemplate";

    public static final String AVRO_CONSUMER_FACTORY = "avroConsumerFactory";

    public static final String AVRO_CONCURRENT_LISTENER_CONTAINER_FACTORY = "avroConcurrentKafkaListenerContainerFactory";

    public static final String USER_AVRO_LISTENER_ID = "userAvroListenerId";

    public static final String USER_AVRO_LISTENER_GROUP = "userAvroListenerGroup";
}
