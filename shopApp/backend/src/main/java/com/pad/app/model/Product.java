package com.pad.app.model;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class Product implements Serializable {

    private Long id;
    private String name;
    private String productCode;
    private int quantity;
    private double price;
    private String status;
    private String type;
    private String subtype;
    private OffsetDateTime created;
    private OffsetDateTime modified;
}
