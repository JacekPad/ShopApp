package com.pad.warehouse.mappers;

import org.mapstruct.Mapper;

import com.pad.warehouse.model.entity.ProductDescriptionEntity;
import com.pad.warehouse.swagger.model.ProductDescription;

@Mapper(componentModel = "spring")
public interface ProductDescriptionMapper {
    
    ProductDescriptionEntity mapToEntityProductDescription(ProductDescription productDescription);

    ProductDescription mapToDataProductDescription(ProductDescriptionEntity productDescription);

}
