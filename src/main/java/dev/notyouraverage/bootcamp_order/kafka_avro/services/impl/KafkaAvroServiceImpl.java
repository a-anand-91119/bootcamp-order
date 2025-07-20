package dev.notyouraverage.bootcamp_order.kafka_avro.services.impl;

import dev.notyouraverage.bootcamp_order.constants.KafkaConstants;
import dev.notyouraverage.bootcamp_order.kafka_avro.services.KafkaAvroService;
import dev.notyouraverage.commons.utils.CompletableFutureUtils;
import dev.notyouraverage.messages.avro.User;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvroServiceImpl implements KafkaAvroService {

    private final String userAvroTopic;

    private final KafkaTemplate<String, SpecificRecord> avroKafkaTemplate;

    public KafkaAvroServiceImpl(
            @Value("${app.kafka.topics.user_avro_message}") String userAvroTopic,
            @Qualifier(KafkaConstants.AVRO_KAFKA_TEMPLATE) KafkaTemplate<String, SpecificRecord> avroKafkaTemplate
    ) {
        this.userAvroTopic = userAvroTopic;
        this.avroKafkaTemplate = avroKafkaTemplate;
    }

    @Override
    public void sendUser(User user) {
        CompletableFutureUtils.unchekedGet(avroKafkaTemplate.send(userAvroTopic, user.getFirstName(), user));
    }
}
