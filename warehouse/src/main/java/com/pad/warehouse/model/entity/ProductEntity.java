package com.pad.warehouse.model.entity;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "b_product")
@Data
public class ProductEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "subtype")
    private String subtype;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    List<ProductDescriptionEntity> productDescription;

    @Column(name = "created", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime created;

    @Column(name = "modified")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime modified;

    @PrePersist
    private void setCreatedDate() {
        created = OffsetDateTime.parse(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @PreUpdate
    private void setModifiedDate() {
        modified = OffsetDateTime.parse(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
