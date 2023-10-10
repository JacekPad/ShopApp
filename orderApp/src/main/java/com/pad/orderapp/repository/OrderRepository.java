package com.pad.orderapp.repository;

import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.warehouse.swagger.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
