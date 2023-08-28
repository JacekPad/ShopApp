package com.pad.app.model.messageTemplates;

import com.pad.app.model.Product;
import lombok.Data;

@Data
public class ProductQuantityChangeContent {
    private int quantity;
    private Long productId;
}
