package com.pad.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.pad.app.model.FilterParams;
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
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ManageProductService manageProductService;

    @Autowired
    private final CacheManager manager;

    public List<ProductList> getProducts(FilterParams params) {
        String name = params.getName();
        log.info("getProducts - START");
        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) manager;
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache("products");
        Cache<Object, Object> caffeine = cache.getNativeCache();
        List<ProductList> productList = caffeine.asMap().values().stream().map(value -> (ProductList) value).toList();
        return filterProducts(params, productList);
    }

    public ProductList getProductDetails(String productId) {
        log.info("getting product details for id: {}", productId);
        return manageProductService.getProduct(productId);
    }

    public boolean isProductAvailable(ProductOrder productOrder) {
        Optional<ProductList> product = Optional.ofNullable(manageProductService.getProduct(productOrder.getProduct().getId()));
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

    public void updateProductCache() {
        try {
            List<ProductList> products = manageProductService.fetchProducts();
            if (products != null) {
                Objects.requireNonNull(manager.getCache("products")).clear();
                products.forEach(manageProductService::populateCache);
            }
        } catch (Exception e) {
            log.error("Error when updating cache: {}", e.getMessage());
        }
    }

    private List<ProductList> filterProducts(FilterParams params, List<ProductList> list) {
        return list.stream()
                .filter(prod -> params.getName() == null || prod.getProduct().getName().contains(params.getName()))
                .filter(prod -> !params.isAvailable() || Integer.parseInt(prod.getProduct().getQuantity()) > 0)
                .filter(prod -> params.getPriceAtMost() == null || Double.parseDouble(prod.getProduct().getPrice()) <= params.getPriceAtMost())
                .filter(prod -> params.getPriceAtLeast() == null || Double.parseDouble(prod.getProduct().getPrice()) >= params.getPriceAtLeast())
                .filter(prod -> params.getType() == null || prod.getProduct().getType().equals(params.getType()))
                .filter(prod -> params.getSubtype() == null || prod.getProduct().getSubtype().equals(params.getSubtype()))
                .collect(Collectors.toList());
    }
}
