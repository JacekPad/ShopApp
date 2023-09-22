package com.pad.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.pad.app.model.ProductOrder;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ManageProductService manageProductService;

    @Autowired
    private final CacheManager manager;
    public List<ProductList> getProducts() {
        log.info("getProducts - START");
        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) manager;
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache("products");
        Cache<Object, Object> caffeine = cache.getNativeCache();
        List<ProductList> productList = caffeine.asMap().values().stream().map(value -> (ProductList) value).toList();
        log.info("GetProducts - END - {}", productList);
        return productList;
    }

    public ProductList getProductDetails(String productId) {
        log.info("getting product details for id: {}", productId);
        return manageProductService.getProduct(productId);
    }

    public boolean isProductAvailable(ProductOrder productOrder) {
        Product orderProduct = productOrder.getProduct();
        List<ProductList> products = getProducts();

        Optional<ProductList> product = products.stream().filter(productList -> productList.getProduct().getId().equals(orderProduct.getId())).findFirst();

        if (product.isPresent()) {
            int productQuantity = Integer.parseInt(product.get().getProduct().getQuantity());
            int quantityBought = productOrder.getQuantityBought();
            return productQuantity - quantityBought >= 0;
        } else {
//            some errors that product doesn't exist?
            return false;
        }
    }

    public void updateProductAvailability(String productId, int quantityChange) {
        manageProductService.updateProductCount(productId, quantityChange);
    }
}
