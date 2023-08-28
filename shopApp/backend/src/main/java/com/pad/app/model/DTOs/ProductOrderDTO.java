package com.pad.app.model.DTOs;

import lombok.Data;

@Data
public class ProductOrderDTO {

    private ProductDTO productDTO;
    private int quantityBought;
    
}
