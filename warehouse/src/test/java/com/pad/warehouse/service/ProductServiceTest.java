package com.pad.warehouse.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.ProductEntity;
import com.pad.warehouse.model.enums.ProductStatus;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductDescription;
import com.pad.warehouse.swagger.model.RequestHeader;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product createProductData(String id) {
        Product product = new Product();
        product.setId(id);
        product.setName("testName");
        product.setProductCode("001");
        product.setQuantity("10");
        product.setPrice("99");
        product.setStatus(ProductStatus.AVAILABLE.getCode());
        product.setType("type");
        product.setSubtype("subtype");
        product.setCreated(null);
        product.setModified(null);
        return product;
    }

    private CreateProductRequest createProductRequest(Product product, List<ProductDescription> descriptions) {
        CreateProductRequest request = new CreateProductRequest();
        RequestHeader header = new RequestHeader();
        header.setRequestId(UUID.randomUUID().toString());
        header.setTimestamp(OffsetDateTime.now());
        request.setRequestHeader(header);
        request.setProduct(product);
        request.setProductDescription(descriptions);
        return request;
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
    void saveProduct_shouldSave() {
        Product product = createProductData(null);
        CreateProductRequest request = createProductRequest(product, null);
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productMapper.mapToEntityProduct(request.getProduct())).thenReturn(createProductEntity(1L));
        // then

        Long saveProductData = productService.saveProductData(request);

        // assert
        assertEquals(1L, saveProductData);
    }

    @Test
    void saveProduct_shouldThrowExceptionIfProductAlreadyExists() {
        Product product = createProductData("1");
        CreateProductRequest request = createProductRequest(product, null);
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity(1L)));
        // then
        try {
            Long saveProductData = productService.saveProductData(request);
            fail("exception not thrown");
        } catch (Exception e) {
            // expected
            assertEquals("Product already exists", e.getMessage());
        }
    }

}
