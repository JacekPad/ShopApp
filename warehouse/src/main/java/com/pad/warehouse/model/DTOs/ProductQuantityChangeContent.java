package com.pad.warehouse.model.DTOs;

import lombok.Data;

@Data
public class ProductQuantityChangeContent {
    private int quantity;
    private Long productId;
}