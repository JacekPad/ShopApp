package com.pad.warehouse.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
import com.pad.warehouse.swagger.model.ProductsResponse;
import com.pad.warehouse.swagger.model.RequestHeader;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductDescriptionService productDescriptionService;

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

    private List<ProductEntity> createEntityList() {
        List<ProductEntity> list = new ArrayList<>();
        list.add(createProductEntity(1L));
        list.add(createProductEntity(2L));
        list.add(createProductEntity(3L));
        return list;
    }

    @Test
    void getProductsData_shouldReturnData() {
        List<ProductEntity> entityList = createEntityList();
        when(productRepository.findByQueryParams(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(), any())).thenReturn(entityList);
        when(productDescriptionService.getDataProductDescriptionsForProduct(any())).thenReturn(List.of());
        // how input many ids?
        when(productMapper.mapToDataProduct(any())).thenReturn(createProductData("1"));
        ProductsResponse productsData = productService.getProductsData("test", "test", "test", "test", "test", "test",
                "test", null, null);
        System.out.println(productsData);
        // assertEquals(entityList, productsData.getProducts());

    }

    @Test
    void getProductsData_shouldThrowNoObjectException() {
        List<ProductEntity> entityList = List.of();
        when(productRepository.findByQueryParams(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(), any())).thenReturn(entityList);
        when(productDescriptionService.getDataProductDescriptionsForProduct(any())).thenReturn(List.of());
        try {
            ProductsResponse productsData = productService.getProductsData("test", "test", "test", "test", "test",
                    "test", "test", null, null);
        } catch (Exception e) {
            assertEquals("No products with given attributes", e.getMessage());
        }

    }

    @Test
    void getProductEntity_shouldReturnEntity() {
        ProductEntity createProductEntity = createProductEntity(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity));
        ProductEntity productEntity = productService.getProductEntity(1L);
        assertEquals(createProductEntity, productEntity);
    }

    @Test
    void getProductEntity_shouldThrowNoObjectFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            productService.getProductEntity(1L);
        } catch (Exception e) {
            assertEquals("No product found", e.getMessage());
        }
    }

    @Test
    void saveProduct_shouldSaveProduct() {
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

    @Test
    void removeProductEntity_shouldReturnEntity() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity(1L)));
        productService.removeProduct("1");
        // TODO finish
    }

    @Test
    void removeProductEntity_shouldThrowNoObjectFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            productService.removeProduct("1");
        } catch (Exception e) {
            assertEquals("No product found", e.getMessage());
        }
    }

    // TODO finish

    @Test
    void updateProductData_shouldReturnProduct() {

    }

    @Test
    void updateProductData_shouldThrowNoObjectFoundException() {

    }

    @Test
    void updateProductData_shouldThrowNoIdSpecifiedException() {

    }

}
