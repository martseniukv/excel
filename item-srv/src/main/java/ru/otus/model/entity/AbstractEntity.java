package ru.otus.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.YesNoConverter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {

    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "update_date", nullable = false)
    private Instant updateDate;
}
