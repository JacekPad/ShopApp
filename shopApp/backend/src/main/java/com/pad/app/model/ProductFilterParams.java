package com.pad.app.model;

import lombok.Data;

@Data
public class ProductFilterParams {

    private String name;
    private boolean isAvailable;
    private Double priceAtMost;
    private Double priceAtLeast;
    private String type;
    private String subtype;

}
