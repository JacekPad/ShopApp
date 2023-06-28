package com.pad.warehouse.mappers;

import org.mapstruct.Mapper;

import com.pad.warehouse.model.data.ProductData;
import com.pad.warehouse.model.entity.Product;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    public abstract Product productFromProductData (ProductData productData);

    public abstract ProductData productFromProductData (Product product);


}