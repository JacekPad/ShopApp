package com.pad.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.pad.app.model.FilterParams;
import com.pad.app.model.ProductOrder;
import com.pad.warehouse.swagger.model.Product;
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

    public List<Product> getProducts(FilterParams params) {
        log.info("getProductsFiltered - Service - START: filterParams: {}", params);
        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) manager;
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache("products");
        Cache<Object, Object> caffeine = cache.getNativeCache();
        List<Product> product = caffeine.asMap().values().stream().map(value -> (Product) value).toList();
        log.info("getProductsFiltered - Service - STOP");
        return filterProducts(params, product);
    }

    public Product getProductDetails(String productId) {
        log.info("getting product details for id: {}", productId);
        return manageProductService.getProduct(productId);
    }

    public boolean isProductAvailable(ProductOrder productOrder) {
        Optional<Product> product = Optional.ofNullable(getProductDetails(productOrder.getProduct().getId()));
        if (product.isPresent()) {
            int productQuantity = Integer.parseInt(product.get().getQuantity());
            int quantityBought = productOrder.getQuantityBought();
            log.info("is available? product {}, {}", product.get(), productQuantity - quantityBought > 0);
            return productQuantity - quantityBought > 0;
        } else {
//            TODO some errors that product doesn't exist?
            return false;
        }
    }

    public void updateProductAvailability(String productId, int quantityChange) {
        log.info("updateProductAvailability - Service - START: for product {}, quantity: {}", productId, quantityChange);
        Product product = manageProductService.getProduct(productId);
        int productQuantity = Integer.parseInt(product.getQuantity());
        product.setQuantity(String.valueOf(productQuantity + quantityChange));
        manageProductService.populateCache(product);
        manageProductService.updateProductDatabase(productId, quantityChange);
        log.info("updateProductAvailability - Service - STOP");
    }

    public void updateProductCache() {
        try {
            List<Product> products = manageProductService.fetchProducts();
            if (products != null) {
                Objects.requireNonNull(manager.getCache("products")).clear();
                products.forEach(manageProductService::populateCache);
            }
        } catch (Exception e) {
            log.error("Error when updating cache: {}", e.getMessage());
        }
    }

    private List<Product> filterProducts(FilterParams params, List<Product> list) {
        return list.stream()
                .filter(prod -> params.getName() == null || prod.getName().contains(params.getName()))
                .filter(prod -> !params.isAvailable() || Integer.parseInt(prod.getQuantity()) > 0)
                .filter(prod -> params.getPriceAtMost() == null || Double.parseDouble(prod.getPrice()) <= params.getPriceAtMost())
                .filter(prod -> params.getPriceAtLeast() == null || Double.parseDouble(prod.getPrice()) >= params.getPriceAtLeast())
                .filter(prod -> params.getType() == null || prod.getType().equals(params.getType()))
                .filter(prod -> params.getSubtype() == null || prod.getSubtype().equals(params.getSubtype()))
                .collect(Collectors.toList());
    }
}
