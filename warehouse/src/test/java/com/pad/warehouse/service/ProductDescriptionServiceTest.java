package com.pad.warehouse.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pad.warehouse.mappers.ProductDescriptionMapper;
import com.pad.warehouse.model.entity.ProductDescriptionEntity;
import com.pad.warehouse.model.entity.ProductEntity;
import com.pad.warehouse.model.enums.ProductStatus;
import com.pad.warehouse.repository.ProductDescriptionRepository;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.ProductDescription;
import com.pad.warehouse.utils.DataValidators;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDescriptionServiceTest {

    @Mock
    private ProductDescriptionRepository productDescriptionRepository;

    @Mock
    private ProductDescriptionMapper productDescriptionMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DataValidators validators;

    @Mock
    private ProductCacheService cacheService;
    
    @InjectMocks
    ProductDescriptionService productDescriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ProductDescription createProductDescription(String id, String productId) {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setId(id);
        productDescription.setProductDescription("test product description");
        productDescription.setProductId(productId);
        return productDescription;
    }

    private ProductDescriptionEntity createProductDescriptionEntity(Long id, Long productId) {
        ProductDescriptionEntity productDescriptionEntity = new ProductDescriptionEntity();
        productDescriptionEntity.setId(id);
        productDescriptionEntity.setProductDescription("test product description");
        productDescriptionEntity.setProductId(productId);
        return productDescriptionEntity;
    }

    private ProductEntity createProductEntity(Long id) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        productEntity.setName("testName");
        productEntity.setProductCode("001");
        productEntity.setQuantity(10);
        productEntity.setPrice(99);
        productEntity.setStatus(ProductStatus.AVAILABLE.getCode());
        productEntity.setType("type");
        productEntity.setSubtype("subtype");
        productEntity.setCreated(null);
        productEntity.setModified(null);
        return productEntity;
    }

    @Test
    void saveProductDescription_shouldSaveProductDescription() {
        ProductDescription productDescription = createProductDescription("1", "1");

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity(1L)));
        when(productDescriptionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productDescriptionMapper.mapToEntityProductDescription(productDescription))
                .thenReturn(createProductDescriptionEntity(1L, 1L));
        when(validators.validateProductDescription(any(), anyMap(), anyBoolean())).thenReturn(true);
                // then
        Long saveProductDescription = productDescriptionService.saveProductDescription(productDescription, 1L);

        assertEquals(1L, saveProductDescription);
    }

    @Test
    void saveProductDescription_shouldThrowExceptionIfProductDescriptionAlreadyExists() {
        ProductDescription productDescription = createProductDescription("1", "1");

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity(1L)));
        when(productDescriptionRepository.findById(anyLong()))
                .thenReturn(Optional.of(createProductDescriptionEntity(1L, 1L)));
        when(productDescriptionMapper.mapToEntityProductDescription(productDescription))
                .thenReturn(createProductDescriptionEntity(1L, 1L));

        // then
        try {
            Long saveProductDescription = productDescriptionService.saveProductDescription(productDescription, 1L);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals("Product description already exists", e.getMessage());
        }
    }

    @Test
    void saveProductDescription_shouldThrowExceptionIfProductDoesNotExist() {
        ProductDescription productDescription = createProductDescription("1", "1");

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productDescriptionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productDescriptionMapper.mapToEntityProductDescription(productDescription))
                .thenReturn(createProductDescriptionEntity(1L, 1L));

        // then
        try {
            Long saveProductDescription = productDescriptionService.saveProductDescription(productDescription, 1L);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals("No product found", e.getMessage());
        }
    }

    @Test
    void removeProductDescription_shouldRemoveProductDescription() {
        // when
        when(productDescriptionRepository.findById(anyLong())).thenReturn(Optional.of(createProductDescriptionEntity(1L, 1L)));

        // then
        String removeProductDescription = productDescriptionService.removeProductDescription(1L);
        assertEquals("Product description removed successfully", removeProductDescription);
    }

    @Test
    void removeProductDescription_shouldThrowErrorIfNoProductDescriptionExists() {
        // when
        when(productDescriptionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // then
        try {
            productDescriptionService.removeProductDescription(1L);
            fail("exception not thrown");
        } catch (Exception e) {
            // expected
            assertEquals("No product description found", e.getMessage());
        }
    }
}
