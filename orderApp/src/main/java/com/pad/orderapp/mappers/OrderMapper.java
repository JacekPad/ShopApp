package com.pad.orderapp.mappers;

import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.warehouse.swagger.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    @Mapping(source = "productOrdered", target = "products")
    Order mapToDataOrder(OrderEntity orderEntity);

    @Mapping(source = "products", target = "productOrdered")
    OrderEntity mapToEntityOrder(Order order);
}
