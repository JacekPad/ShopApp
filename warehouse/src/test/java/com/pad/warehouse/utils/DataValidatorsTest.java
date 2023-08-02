package com.pad.warehouse.utils;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pad.warehouse.service.ProductCacheService;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductDescription;

public class DataValidatorsTest {

    @Mock
    private ProductCacheService cacheService;

    @InjectMocks
    DataValidators validators;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product createEmptyProduct() {
        Product product = new Product();
        product.setId(null);
        product.setName(null);
        product.setProductCode(null);
        product.setQuantity(null);
        product.setPrice(null);
        product.setStatus(null);
        product.setType(null);
        product.setSubtype(null);
        product.setCreated(null);
        product.setModified(null);
        return product;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId("1");
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

    private ProductDescription createProductDescription() {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setId("1");
        productDescription.setProductDescription("test product description");
        productDescription.setProductId("1");
        return productDescription;
    }

    private ProductDescription createEmptyDescription() {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setId(null);
        productDescription.setProductDescription(null);
        productDescription.setProductId(null);
        return productDescription;
    }

    @Test
    void addProduct_shouldReturnTrue() {
        // when
        Product product = createProduct();
        Map<String, String> errors = new HashMap<>();
        when(cacheService.getStatusMap()).thenReturn(Map.of("STATUS", "status"));
        when(cacheService.getTypeMap()).thenReturn(Map.of("TYPE", "type"));
        when(cacheService.getSubtypeMap()).thenReturn(Map.of("SUBTYPE", "subtype"));
        validators.validateProduct(product, errors, true);
        // then
        assertTrue(errors.isEmpty());
    }

    @Test
    void addProduct_shouldReturnFalse_EmptyFields() {
        // when
        Product product = createEmptyProduct();
        Map<String, String> errors = new HashMap<>();
        validators.validateProduct(product, errors, true);
        // then
        assertTrue(!errors.isEmpty());
        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("productCode"));
        assertTrue(errors.containsKey("quantity"));
        assertTrue(errors.containsKey("price"));
        assertTrue(errors.containsKey("status"));
        assertTrue(errors.containsKey("type"));
        assertTrue(errors.containsKey("subtype"));
    }

    @Test
    void addProduct_shouldReturnFalse_WrongEnumValues() {
        // when
        Product product = createProduct();
        product.setStatus("1234");
        product.setType("1234");
        product.setSubtype("1234");
        Map<String, String> errors = new HashMap<>();
        when(cacheService.getStatusMap()).thenReturn(Map.of("STATUS", "status"));
        when(cacheService.getTypeMap()).thenReturn(Map.of("TYPE", "type"));
        when(cacheService.getSubtypeMap()).thenReturn(Map.of("SUBTYPE", "subtype"));
        validators.validateProduct(product, errors, true);
        // then
        assertTrue(!errors.isEmpty());
        assertTrue(!errors.containsKey("name"));
        assertTrue(!errors.containsKey("productCode"));
        assertTrue(!errors.containsKey("quantity"));
        assertTrue(!errors.containsKey("price"));
        assertTrue(errors.containsKey("status"));
        assertTrue(errors.containsKey("type"));
        assertTrue(errors.containsKey("subtype"));
    }

    @Test 
    void updateProduct_shouldReturnTrue_emptyFields() {
        // when
        Product product = createEmptyProduct();
        Map<String, String> errors = new HashMap<>();
        product.setName("editedNameTest");
        validators.validateProduct(product, errors, false);
        // then
        assertTrue(errors.isEmpty());
    }

    @Test 
    void updateProduct_shouldReturnTrue() {
        // when
        Product product = createProduct();
        Map<String, String> errors = new HashMap<>();
        when(cacheService.getStatusMap()).thenReturn(Map.of("STATUS", "status"));
        when(cacheService.getTypeMap()).thenReturn(Map.of("TYPE", "type"));
        when(cacheService.getSubtypeMap()).thenReturn(Map.of("SUBTYPE", "subtype"));
        validators.validateProduct(product, errors, false);
        // then
        assertTrue(errors.isEmpty());
    }

    @Test
    void updateProduct_shouldReturnFalse_WrongEnumValues() {
        // when
        Product product = createProduct();
        Map<String, String> errors = new HashMap<>();
        product.setStatus("wrongStatus");
        product.setType("wrongType");
        product.setSubtype("wrongSubtype");
        when(cacheService.getStatusMap()).thenReturn(Map.of("STATUS", "status"));
        when(cacheService.getTypeMap()).thenReturn(Map.of("TYPE", "type"));
        when(cacheService.getSubtypeMap()).thenReturn(Map.of("SUBTYPE", "subtype"));
        validators.validateProduct(product, errors, false); 

        // then
        assertTrue(!errors.isEmpty());
        assertTrue(!errors.containsKey("name"));
        assertTrue(!errors.containsKey("productCode"));
        assertTrue(!errors.containsKey("quantity"));
        assertTrue(!errors.containsKey("price"));
        assertTrue(errors.containsKey("status"));
        assertTrue(errors.containsKey("type"));
        assertTrue(errors.containsKey("subtype"));
    }

    @Test
    void updateProduct_shouldReturnFalse_wrongQuantityParse() {
        // when
        Product productNumberException = createEmptyProduct();
        productNumberException.setQuantity("abc");
        Map<String, String> errorsNumberException = new HashMap<>();

        Product productNegativeQuantity = createEmptyProduct();
        productNegativeQuantity.setQuantity("-1");
        Map<String, String> errorsNegativeQuantity = new HashMap<>();

        Product productCorrect = createEmptyProduct();
        productCorrect.setQuantity("65");
        Map<String, String> errorsCorrect = new HashMap<>();

        validators.validateProduct(productNumberException, errorsNumberException, false);
        validators.validateProduct(productNegativeQuantity, errorsNegativeQuantity, false);
        validators.validateProduct(productCorrect, errorsCorrect, false);
        
        // then
        assertTrue(!errorsNegativeQuantity.isEmpty());
        assertTrue(errorsNumberException.containsKey("quantity"));
        assertEquals("quantity must be a valid integer", errorsNumberException.get("quantity"));

        assertTrue(!errorsNegativeQuantity.isEmpty());
        assertTrue(errorsNegativeQuantity.containsKey("quantity"));
        assertEquals("quantity must be a postive integer", errorsNegativeQuantity.get("quantity"));

        assertTrue(errorsCorrect.isEmpty());
    }

    @Test
    void addProduct_shouldReturnFalse_wrongPriceParse() {
        // when
        Product productNumberException = createEmptyProduct();
        productNumberException.setPrice("abc");
        Map<String, String> errorsNumberException = new HashMap<>();

        Product productNegativeQuantity = createEmptyProduct();
        productNegativeQuantity.setPrice("-1.12");
        Map<String, String> errorsNegativeQuantity = new HashMap<>();

        Product productCorrect = createEmptyProduct();
        productCorrect.setPrice("33.22");
        Map<String, String> errorsCorrect = new HashMap<>();

        validators.validateProduct(productNumberException, errorsNumberException, false);
        validators.validateProduct(productNegativeQuantity, errorsNegativeQuantity, false);
        validators.validateProduct(productCorrect, errorsCorrect, false);
        
        // then
        assertTrue(!errorsNumberException.isEmpty());
        assertTrue(errorsNumberException.containsKey("price"));
        assertEquals("price must be a valid number", errorsNumberException.get("price"));

        assertTrue(!errorsNegativeQuantity.isEmpty());
        assertTrue(errorsNegativeQuantity.containsKey("price"));
        assertEquals("price must be a positive number", errorsNegativeQuantity.get("price"));

        assertTrue(errorsCorrect.isEmpty());
    }

    @Test
    void addProductDescription_shouldReturnTrue() {
        // when
        ProductDescription productDescription = createProductDescription();
        Map<String, String> errorsCorrect = new HashMap<>();
        validators.validateProductDescription(productDescription, errorsCorrect, true);

        // then
        assertTrue(errorsCorrect.isEmpty());
    }

    @Test
    void addProductDescription_shouldReturnFalse_emptyFields() {
        // when
        ProductDescription productDescription = createEmptyDescription();
        Map<String, String> errorsEmptyFields = new HashMap<>();
        validators.validateProductDescription(productDescription, errorsEmptyFields, true);

        // then
        assertTrue(!errorsEmptyFields.isEmpty());
        assertTrue(errorsEmptyFields.containsKey("product"));
        assertTrue(errorsEmptyFields.containsKey("description"));

    }

    @Test 
    void updateProductDescription_shouldReturnTrue_emptyFields() {
        // when
        ProductDescription productDescription = createEmptyDescription();
        Map<String, String> errorsCorrect = new HashMap<>();
        productDescription.setProductDescription("editedDescription");
        validators.validateProductDescription(productDescription, errorsCorrect, false);

        // then
        assertTrue(errorsCorrect.isEmpty());
    }
    
}
