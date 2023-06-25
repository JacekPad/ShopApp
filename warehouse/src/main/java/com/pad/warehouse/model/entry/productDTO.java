package com.pad.warehouse.model.entry;

import java.util.List;

import com.pad.warehouse.model.data.ProductData;
import com.pad.warehouse.model.entity.ProductDescription;

import lombok.Data;

@Data
public class ProductDTO {

    private ProductData product;

    List<ProductDescription> productDescriptions;
}
