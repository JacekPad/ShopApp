package com.pad.app.service;

import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Value("${product.get-all.uri}")
    private String PRODUCT_URI;

    private final WebClientService webClientService;
    
    public List<ProductList> getProducts() {
//        TODO cache caffeine it
        List<ProductList> productLists = retrieveProducts();
        return productLists;
    }

    private List<ProductList> retrieveProducts() {
        ProductsResponse products = webClientService.webClientGet(PRODUCT_URI, ProductsResponse.class);
        log.info("retrieved prodcuts: {}", products);
        return products.getProducts();
    }

    public void getProductDetails() {
        // get details of product
    }


    public boolean isProductAvailable(Long id) {
        // check if available and update cached value?
//        TODO maybe send with rabbitMQ all product as multithread and wait for each response?
//        updateProductAvailability(id);
        return true;
    }

    private void updateProductAvailability(Long id) {
        // update cached quantity value of checked item? (is it possible?)
    }
}
