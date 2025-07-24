package kr.hhplus.be.server.user.infrastructure.persistence.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "CREATED_AT", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "UPDATED_AT")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}