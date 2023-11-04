package com.pad.orderapp.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.swagger.model.DeliveryMethodEnum;
import com.pad.orderapp.swagger.model.PaymentMethodEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "b_orders")
@ToString
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_payed")
    private boolean isPayed;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethodEnum deliveryMethod;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<ProductOrderEntity> productOrdered;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private AddressEntity address;

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
