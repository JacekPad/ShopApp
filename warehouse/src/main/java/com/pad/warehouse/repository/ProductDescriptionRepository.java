package com.pad.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pad.warehouse.model.entity.ProductDescriptionEntity;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescriptionEntity, Long> {
    
    List<ProductDescriptionEntity> getByProductId(Long productId);

    @Query("SELECT COUNT(*) FROM ProductDescriptionEntity pd WHERE pd.productId = :productId")
    Long getCountByProductId(Long productId);
}
