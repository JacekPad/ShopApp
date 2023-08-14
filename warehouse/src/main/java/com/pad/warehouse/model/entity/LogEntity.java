package com.pad.warehouse.model.entity;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pad.warehouse.model.enums.ProductLogType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "t_application_logs")
@Data
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductLogType type;

    @Column(name = "timestamp", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime timestamp;

    @PrePersist
    private void setCreatedDate() {
        timestamp = OffsetDateTime.parse(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
