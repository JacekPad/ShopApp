package com.pad.app.service;

import com.pad.app.model.ProductFilterParams;
import com.pad.app.swagger.model.Product;
import com.pad.app.swagger.model.ProductOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {




    @Mock
    private ManageProductService manageProductService;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product prepareProductList(String id) {
        Product product = new Product();
        product.setId(id);
        product.setName("testName");
        product.setProductCode("test001");
        product.setQuantity("5");
        product.setPrice("5.55");
        product.setStatus("testStatus");
        product.setType("testType");
        product.setSubtype("testSubtype");
        return product;
    }

    private ProductOrder prepareProductOrder(String productId, int quantity) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantityBought(String.valueOf(quantity));
        productOrder.setProductId(prepareProductList(productId).getId());
        return productOrder;
    }

    private ProductFilterParams prepareFilterParams(String name, String type, String subtype, boolean available,
                                                    Double priceAtMost, Double priceAtLeast) {
        ProductFilterParams params = new ProductFilterParams();
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


}