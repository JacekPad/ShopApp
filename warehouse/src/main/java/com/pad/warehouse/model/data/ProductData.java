package com.pad.warehouse.model.data;

import lombok.Data;

@Data
public class ProductData {

    private String id;

    private String name;

    private String productCode;

    private String quantity;

    private String price;

    private String status;

    private String type;

    private String subType;

    private String created;

    private String modified;
    
}
