package com.pad.warehouse.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pad.warehouse.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
