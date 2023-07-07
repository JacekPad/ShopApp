package com.pad.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pad.warehouse.model.entity.ProductDescription;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {
    
    List<ProductDescription> getByProductId(Long productId);
}
