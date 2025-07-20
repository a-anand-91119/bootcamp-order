package dev.notyouraverage.bootcamp_order.document_db.dtos;

import dev.notyouraverage.bootcamp_order.document_db.enums.OrderEventType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEventDocumentDTO {
    private String id;

    private String orderId;

    private OrderEventType eventType;

    private Map<String, Object> payload;

    private LocalDateTime timestamp;

    private String eventSource;

    private Integer eventVersion;

    private Date createdAt;

    private Date updatedAt;

    private Long version;
}
