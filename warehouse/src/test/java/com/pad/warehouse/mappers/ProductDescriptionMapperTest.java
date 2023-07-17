package com.pad.warehouse.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pad.warehouse.model.entity.ProductDescriptionEntity;
import com.pad.warehouse.swagger.model.ProductDescription;

public class ProductDescriptionMapperTest {

    private ProductDescriptionMapper productDescriptionMapper;

    @BeforeEach
    public void setUp() {
        this.productDescriptionMapper = new ProductDescriptionMapperImpl();
    }

    private ProductDescription createProductDescription() {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setId("1");
        productDescription.setProductDescription("Test description data");
        productDescription.setProductId("1");
        return productDescription;
    }

    private ProductDescriptionEntity createProductDescriptionEntity() {
        ProductDescriptionEntity productDescriptionEntity = new ProductDescriptionEntity();
        productDescriptionEntity.setId(1L);
        productDescriptionEntity.setProductId(1L);
        productDescriptionEntity.setProductDescription("Test description entity");
        return productDescriptionEntity;
    }

    @Test
    void mapEntityToData_shouldReturnData() {
        ProductDescription productDescriptionData = createProductDescription();
        ProductDescriptionEntity productDescriptionEntity = productDescriptionMapper.mapToEntityProductDescription(productDescriptionData);
        assertNotNull(productDescriptionEntity);
        
        assertEquals(productDescriptionData.getId(), String.valueOf(productDescriptionEntity.getId()));
        assertEquals(productDescriptionData.getProductId(), String.valueOf(productDescriptionEntity.getProductId()));
        assertEquals(productDescriptionData.getProductDescription(), productDescriptionEntity.getProductDescription());
    }

    @Test
    void mapDataToEntity_shouldReturnEntity() {
        ProductDescriptionEntity productDescriptionEntity = createProductDescriptionEntity();
        ProductDescription productDescriptionData = productDescriptionMapper.mapToDataProductDescription(productDescriptionEntity);

        assertNotNull(productDescriptionData);
        assertEquals(String.valueOf(productDescriptionEntity.getId()), productDescriptionData.getId());
        assertEquals(String.valueOf(productDescriptionEntity.getProductId()), productDescriptionData.getProductId());
        assertEquals(productDescriptionEntity.getProductDescription(), productDescriptionData.getProductDescription());
    }

}
