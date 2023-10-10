package com.pad.orderapp.repository;

import com.pad.orderapp.model.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {
}
