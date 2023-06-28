package com.pad.warehouse.model.entry;

import java.util.List;

import com.pad.warehouse.model.data.ProductData;
import com.pad.warehouse.model.data.ProductDescriptionData;

import lombok.Data;

@Data
public class ProductDTO {

    private ProductData product;

    List<ProductDescriptionData> productDescriptions;
}
