package com.pad.warehouse.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pad.warehouse.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE (:name is null OR p.name = :name) AND (:productCode is null OR p.productCode = :productCode) AND (:quantity is null OR p.quantity = :quantity) AND (:price is null OR p.price = :price) AND (:status is null OR p.status = :status) AND (:type is null OR p.type = :type) AND (:subtype is null OR p.subtype = :subtype) AND (:created is null or p.created = :created) AND (:modified is null OR p.modified = :modified)")
    List<Product> findByQueryParams(@Param("name") String name, @Param("productCode") String productCode, @Param("quantity") String quantity, @Param("price") String price, @Param("status") String status, @Param("type") String type, @Param("subtype") String subtype, @Param("created") String created, @Param("modified") String modified);


}
