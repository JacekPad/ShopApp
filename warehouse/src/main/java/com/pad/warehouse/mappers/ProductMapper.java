package com.pad.warehouse.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pad.warehouse.model.entity.ProductEntity;
import com.pad.warehouse.swagger.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    ProductEntity mapToEntityProduct(Product product);

    Product mapToDataProduct(ProductEntity product);

}
