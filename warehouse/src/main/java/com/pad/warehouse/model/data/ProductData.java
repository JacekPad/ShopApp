package com.pad.warehouse.model.data;

import java.time.LocalDateTime;

import com.pad.warehouse.model.entity.Product;

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


    public Product convertFrom(ProductData productData) {
        Product product = new Product();
        product.setId(Long.valueOf(productData.getId()));
        product.setName(productData.getName());
        product.setProductCode(productData.getProductCode());
        product.setQuantity(Integer.parseInt(productData.getQuantity()));
        product.setPrice(Double.parseDouble(productData.getPrice()));
        product.setStatus(productData.getStatus());
        product.setType(productData.getType());
        product.setSubType(productData.getSubType());
        product.setCreated(LocalDateTime.parse(productData.getCreated()));
        product.setModified(LocalDateTime.parse(productData.getModified()));
        return product;
    }
    
}
