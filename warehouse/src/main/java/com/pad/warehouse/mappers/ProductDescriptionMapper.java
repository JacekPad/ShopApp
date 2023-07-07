package com.pad.warehouse.mappers;

import org.mapstruct.Mapper;

import com.pad.warehouse.model.entity.ProductDescription;

@Mapper(componentModel = "spring")
public interface ProductDescriptionMapper {
    
    ProductDescription mapToEntityProductDescription(com.pad.warehouse.swagger.model.ProductDescription productDescription);

    com.pad.warehouse.swagger.model.ProductDescription mapToDataProductDescription(ProductDescription productDescription);

}
