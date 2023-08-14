package com.pad.warehouse.model.entity;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "t_error_logs")
@Data
public class ErrorLogEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "message")
    private String message;

    @Column(name = "trace")
    private String stackTrace;
    
    @Column(name = "timestamp", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime timestamp;

    @PrePersist
    private void setCreatedDate() {
        timestamp = OffsetDateTime.parse(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
