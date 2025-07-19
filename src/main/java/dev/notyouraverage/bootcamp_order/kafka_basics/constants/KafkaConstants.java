package dev.notyouraverage.bootcamp_order.kafka_basics.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {
    public static final String JSON_SERIALIZABLE_PRODUCER_FACTORY = "jsonSerializableProducerFactory";

    public static final String JSON_SERIALIZABLE_KAFKA_TEMPLATE = "jsonSerializableKafkaTemplate";

    public static final String JSON_SERIALIZABLE_CONSUMER_FACTORY = "jsonSerializableConsumerFactory";

    public static final String JSON_SERIALIZABLE_CONCURRENT_LISTENER_CONTAINER_FACTORY = "jsonSerializableConcurrentKafkaListenerContainerFactory";

    public static final String TRUSTED_PACKAGES = "dev.notyouraverage.messages";

    public static final String ORDER_CREATED_LISTENER_ID = "OrderCreatedListenerId";

    public static final String ORDER_CREATED_LISTENER = "OrderCreatedListener";
}
