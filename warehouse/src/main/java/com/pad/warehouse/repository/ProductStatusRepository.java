package com.pad.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pad.warehouse.model.entity.ProductStatus;

public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
    
}
