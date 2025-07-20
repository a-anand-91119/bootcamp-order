package dev.notyouraverage.bootcamp_order.document_db.documents;

import dev.notyouraverage.bootcamp_order.document_db.constants.MongoConstants;
import dev.notyouraverage.bootcamp_order.document_db.enums.Status;
import java.util.Date;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class BaseDocument {
    @Id
    private String mongoId;

    @Field(MongoConstants.ID)
    private String id;

    @Field(MongoConstants.CREATED_AT)
    @CreatedDate
    private Date createdAt;

    @Field(MongoConstants.UPDATED_AT)
    @LastModifiedDate
    private Date updatedAt;

    @Field(MongoConstants.VERSION)
    @Version
    private Long version;

    @Field(MongoConstants.STATUS)
    private Status documentStatus;
}
