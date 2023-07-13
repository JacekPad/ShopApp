package com.pad.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pad.warehouse.model.entity.ProductDescriptionEntity;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescriptionEntity, Long> {
    
    List<ProductDescriptionEntity> getByProductId(Long productId);
}
