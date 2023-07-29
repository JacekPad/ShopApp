package com.pad.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pad.warehouse.model.entity.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    
}
