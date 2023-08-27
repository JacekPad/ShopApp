package com.pad.app.model;

import lombok.Data;

@Data
public class ProductOrder {

    private Product product;
    private int quantityBought;
    
}
