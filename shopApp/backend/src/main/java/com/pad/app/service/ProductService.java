package com.pad.app.service;

import com.pad.app.model.ProductOrder;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Value("${product.get-all.uri}")
    private String PRODUCT_URI;

    private final WebClientService webClientService;

    @Cacheable("products")
    public List<ProductList> getProducts() {
//        TODO cache caffeine it
        List<ProductList> productLists = retrieveProducts();
        return productLists;
    }

    private List<ProductList> retrieveProducts() {
        log.info("No products in cache, requesting products");
        ProductsResponse products = webClientService.webClientGet(PRODUCT_URI, ProductsResponse.class);
        log.info("retrieved products from database: {}", products);
        return products.getProducts();
    }

    public void getProductDetails() {
        // get details of product
    }


    public boolean isProductAvailable(ProductOrder productOrder) {
        Product orderProduct = productOrder.getProduct();

        // check if available and update cached value?

        return true;
    }

    private void updateProductAvailability(Long id) {

        // update cached quantity value of checked item? (is it possible?)
    }

    private void updateCacheValues() {

    }
}
