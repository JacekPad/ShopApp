package com.pad.warehouse.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pad.warehouse.model.entity.ProductEntity;
import com.pad.warehouse.model.enums.ProductStatus;
import com.pad.warehouse.swagger.model.Product;

public class ProductMapperTest {

    
    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        this.productMapper = new ProductMapperImpl();
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId("1");
        product.setName("Test name");
        product.setProductCode("001");
        product.setQuantity("1");
        product.setPrice("99.0");
        product.setStatus(ProductStatus.AVAILABLE.getCode());
        product.setType("type");
        product.setSubtype("subtype");
        product.setCreated(OffsetDateTime.now());
        product.setModified(OffsetDateTime.now());
        return product;
    }

    private ProductEntity createProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Test name");
        productEntity.setProductCode("001");
        productEntity.setQuantity(1);
        productEntity.setPrice(99);
        productEntity.setStatus(ProductStatus.AVAILABLE.getCode());
        productEntity.setType("type");
        productEntity.setSubtype("subtype");
        productEntity.setCreated(OffsetDateTime.now());
        productEntity.setModified(OffsetDateTime.now());
        return productEntity;
    }

    @Test
    void mapDataToEntity_shouldReturnData() {
        Product product = createProduct();
        ProductEntity productEntity = productMapper.mapToEntityProduct(product);
        assertNotNull(product);
        
        assertEquals(product.getId(), String.valueOf(productEntity.getId()));
        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getProductCode(), productEntity.getProductCode());
        assertEquals(product.getQuantity(), String.valueOf(productEntity.getQuantity()));
        assertEquals(product.getPrice(), String.valueOf(productEntity.getPrice()));
        assertEquals(product.getStatus(), String.valueOf(productEntity.getStatus()));
        assertEquals(product.getType(), String.valueOf(productEntity.getType()));
        assertEquals(product.getSubtype(), String.valueOf(productEntity.getSubtype()));
        assertEquals(null, productEntity.getCreated());
        assertEquals(null, productEntity.getModified());
    }

    @Test
    void mapEntityToData_shouldReturnEntity() {
        ProductEntity productEntity = createProductEntity();
        Product product = productMapper.mapToDataProduct(productEntity);

        assertNotNull(product);
        assertEquals(String.valueOf(productEntity.getId()), product.getId());
        assertEquals(productEntity.getName(), product.getName());
        assertEquals(productEntity.getProductCode(), product.getProductCode());
        assertEquals(String.valueOf(productEntity.getQuantity()), product.getQuantity());
        assertEquals(String.valueOf(productEntity.getPrice()), product.getPrice());
        assertEquals(String.valueOf(productEntity.getStatus()), product.getStatus());
        assertEquals(String.valueOf(productEntity.getType()), product.getType());
        assertEquals(String.valueOf(productEntity.getSubtype()), product.getSubtype());
        assertEquals(productEntity.getCreated(), product.getCreated());
        assertEquals(productEntity.getModified(), product.getModified());
    }
}
