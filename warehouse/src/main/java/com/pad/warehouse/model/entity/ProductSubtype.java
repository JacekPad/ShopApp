package com.pad.warehouse.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "e_product_subtype")
public class ProductSubtype {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_code")
    private String typeName;

    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "type_code", insertable = false, updatable = false)
    @JsonIgnore
    private ProductType productType;
}
