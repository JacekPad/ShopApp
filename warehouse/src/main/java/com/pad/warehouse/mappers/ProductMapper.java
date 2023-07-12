package com.pad.warehouse.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pad.warehouse.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    Product mapToEntityProduct(com.pad.warehouse.swagger.model.Product product);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    com.pad.warehouse.swagger.model.Product mapToDataProduct(Product product);

}
