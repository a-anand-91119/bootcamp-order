package dev.notyouraverage.bootcamp_order.kickoff.models.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Base {

    @Column(name = "is_deleted")
    protected boolean isDeleted;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 50)
    protected String createdBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    protected LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    protected String lastModifiedBy;
}
