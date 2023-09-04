package com.pad.app.model;

import com.pad.warehouse.swagger.model.Product;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProductOrder implements Serializable {

    private Product product;
    private int quantityBought;
    
}
