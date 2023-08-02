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
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductDescription;
import com.pad.warehouse.swagger.model.ProductsResponse;
import com.pad.warehouse.swagger.model.RequestHeader;
import com.pad.warehouse.utils.DataValidators;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductDescriptionService productDescriptionService;

    @Mock
    private ProductCacheService cacheService;

    @Mock
    private DataValidators validators;

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
        product.setPrice("99.0");
        product.setStatus("STATUS");
        product.setType("TYPE");
        product.setSubtype("SUBTYPE");
        product.setCreated(OffsetDateTime.parse("2023-07-27T16:50:27.195414700+02:00"));
        product.setModified(OffsetDateTime.parse("2023-07-27T16:50:27.195414700+02:00"));
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
        productEntity.setStatus("STATUS");
        productEntity.setType("TYPE");
        productEntity.setSubtype("SUBTYPE");
        productEntity.setCreated(OffsetDateTime.parse("2023-07-27T16:50:27.195414700+02:00"));
        productEntity.setModified(OffsetDateTime.parse("2023-07-27T16:50:27.195414700+02:00"));
        return productEntity;
    }

    private List<ProductEntity> createEntityList() {
        List<ProductEntity> list = new ArrayList<>();
        list.add(createProductEntity(1L));
        list.add(createProductEntity(2L));
        list.add(createProductEntity(3L));
        return list;
    }

    private boolean isProductEqual(ProductEntity productEntity, Product product) {
        return String.valueOf(productEntity.getId()).equals(product.getId())
        && productEntity.getName().equals(product.getName())
        && productEntity.getProductCode().equals(product.getProductCode())
        && String.valueOf(productEntity.getQuantity()).equals(product.getQuantity())
        && String.valueOf(productEntity.getPrice()).equals(product.getPrice())
        && productEntity.getStatus().equals(product.getStatus())
        && productEntity.getType().equals(product.getType())
        && productEntity.getSubtype().equals(product.getSubtype())
        && String.valueOf(productEntity.getCreated()).equals(String.valueOf(product.getCreated()))
        && String.valueOf(productEntity.getModified()).equals(String.valueOf(product.getModified()));
    }

    @Test
    void getProductsData_shouldReturnData() {
        // when
        List<ProductEntity> entityList = createEntityList();
        when(productRepository.findByQueryParams(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(), any())).thenReturn(entityList);
        when(productDescriptionService.getDataProductDescriptionsForProduct(any())).thenReturn(List.of());
        when(productMapper.mapToDataProduct(entityList.get(0))).thenReturn(createProductData("1"));
        when(productMapper.mapToDataProduct(entityList.get(1))).thenReturn(createProductData("2"));
        when(productMapper.mapToDataProduct(entityList.get(2))).thenReturn(createProductData("3"));
        
        // then
        ProductsResponse productsData = productService.getProductsData("test", "test", "test", "test", "test", "test",
                "test", null, null);
        assertTrue(isProductEqual(entityList.get(0), productsData.getProducts().get(0).getProduct()));
        assertTrue(isProductEqual(entityList.get(1), productsData.getProducts().get(1).getProduct()));
        assertTrue(isProductEqual(entityList.get(2), productsData.getProducts().get(2).getProduct()));
    }

    @Test
    void getProductsData_shouldThrowNoObjectException() {
        // when
        List<ProductEntity> entityList = List.of();
        when(productRepository.findByQueryParams(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(), any())).thenReturn(entityList);
        when(productDescriptionService.getDataProductDescriptionsForProduct(any())).thenReturn(List.of());

        // then 
        try {
            ProductsResponse productsData = productService.getProductsData("test", "test", "test", "test", "test",
                    "test", "test", null, null);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals("No products with given attributes", e.getMessage());
        }

    }

    @Test
    void getProductEntity_shouldReturnEntity() {
        // when
        ProductEntity createProductEntity = createProductEntity(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity));

        // then 
        ProductEntity productEntity = productService.getProductEntity(1L);
        assertEquals(createProductEntity, productEntity);
    }

    @Test
    void getProductEntity_shouldThrowNoObjectFoundException() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        try {
            productService.getProductEntity(1L);
            fail("exception not thrown");
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
        when(validators.validateProduct(any(), anyMap(), anyBoolean())).thenReturn(true);
        // then
        Long saveProductData = productService.saveProductData(request);
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
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProductEntity(1L)));
        when(productDescriptionService.getEntityProductDescriptionForProduct(anyLong())).thenReturn(List.of());

        // then
        String removeProduct = productService.removeProduct("1");
        assertEquals("Product removed successfully", removeProduct);
    }

    @Test
    void removeProductEntity_shouldThrowNoObjectFoundException() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        try {
            productService.removeProduct("1");
            fail("exception not thrown");
        } catch (Exception e) {
            // expected
            assertEquals("No product found", e.getMessage());
        }
    }

    @Test
    void updateProductData_shouldThrowNoObjectFoundException() {
        // when
        Product editData = new Product();
        editData.setName("editedName");
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(validators.validateProduct(any(), anyMap(), anyBoolean())).thenReturn(true);

        // then
        try {
            productService.updateProductData("1", editData);
            fail("exception not thrown");
        } catch (Exception e) {
            // expected
            assertEquals("Product does not exists", e.getMessage());
        }        
    }

    @Test
    void updateProductData_shouldThrowNoIdSpecifiedException() {
        // when 
        Product editData = new Product();
        editData.setName("editedName");

        // then
        try {
            productService.updateProductData(null, editData);
            fail("exception not thrown");
        } catch (Exception e) {
            // expected
            assertEquals("Product id could not be specified", e.getMessage());
        }
    }

}
