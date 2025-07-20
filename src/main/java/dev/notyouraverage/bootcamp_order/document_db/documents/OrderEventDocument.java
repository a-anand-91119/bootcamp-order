package dev.notyouraverage.bootcamp_order.document_db.documents;

import dev.notyouraverage.bootcamp_order.document_db.constants.MongoConstants;
import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Document(MongoConstants.COLLECTION_ORDER_EVENTS)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEventDocument extends BaseDocument {

    @Field("orderId")
    private String orderId;

    @Field("eventType")
    private OrderEventType eventType;

    @Field("payload")
    private Map<String, Object> payload;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("eventSource")
    private String eventSource;

    @Field("eventVersion")
    private Integer eventVersion;
}
