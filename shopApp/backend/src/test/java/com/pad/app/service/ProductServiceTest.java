package com.pad.app.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.pad.app.model.FilterParams;
import com.pad.app.model.ProductOrder;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductList;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {


//TODO learn how to test cache?


    @Mock
    private ManageProductService manageProductService;

//    @Mock
//    private CacheManager manager;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        this.manager = prepareCacheManager():
//        populateManager();
    }


//    private CacheManager prepareCacheManager() {
//        CaffeineCache productsCache = new CaffeineCache("products",
//                Caffeine.newBuilder().build());
//        CaffeineCacheManager manager = new CaffeineCacheManager();
//        manager.setCacheNames(Collections.singletonList("products"));
//        return manager;
//    }
//
//    private void populateManager() {
//        manager.getCache("products").put("1", prepareProductList("1"));
//    }

    private ProductList prepareProductList(String id) {
        ProductList productList = new ProductList();
        Product product = new Product();
        product.setId(id);
        product.setName("testName");
        product.setProductCode("test001");
        product.setQuantity("5");
        product.setPrice("5.55");
        product.setStatus("testStatus");
        product.setType("testType");
        product.setSubtype("testSubtype");
        productList.setProduct(product);
        return productList;
    }

    private ProductOrder prepareProductOrder(String productId, int quantity) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantityBought(quantity);
        productOrder.setProduct(prepareProductList(productId).getProduct());
        return productOrder;
    }

    private FilterParams prepareFilterParams(String name, String type, String subtype, boolean available,
                                             Double priceAtMost, Double priceAtLeast) {
        FilterParams params = new FilterParams();
        params.setName(name);
        params.setType(type);
        params.setSubtype(subtype);
        params.setAvailable(available);
        params.setPriceAtMost(priceAtMost);
        params.setPriceAtLeast(priceAtLeast);
        return params;
    }


    @Test
    void isProductAvailable_whenNoProduct_shouldThrowException() {
        String productId = "1";
        ProductOrder testProductOrder = prepareProductOrder(productId, 2);
        when(manageProductService.getProduct(anyString())).thenReturn(null);
        boolean productAvailable = service.isProductAvailable(testProductOrder);

        assertFalse(productAvailable);
    }

    @Test
    void isProductAvailable_whenProductAvailable_shouldReturnTrue() {
        String productId = "1";
        ProductOrder testProductOrder = prepareProductOrder(productId, 2);
        when(manageProductService.getProduct(anyString())).thenReturn(prepareProductList(productId));
        boolean productAvailable = service.isProductAvailable(testProductOrder);
        assertTrue(productAvailable);
    }

    @Test
    void isProductAvailable_whenProductNotAvailable_shouldReturnFalse() {
        String productId = "1";
        ProductOrder testProductOrder = prepareProductOrder(productId, 10);
        when(manageProductService.getProduct(anyString())).thenReturn(prepareProductList(productId));
        boolean productAvailable = service.isProductAvailable(testProductOrder);
        assertFalse(productAvailable);
    }

    @Test
    void getProducts_whenProductsMatchParams_shouldReturnFilteredProductList() {
//        List<ProductList> productList = service.getProducts(prepareFilterParams("name", null, null, true,
//                null, null));
//

    }

    @Test
    void getProducts_whenNoProductsMatchParams_shouldReturnEmptyList() {


    }


}