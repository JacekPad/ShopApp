package com.pad.app.service;

import com.pad.warehouse.swagger.model.ProductList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class SchedulerService {

    private final ManageProductService manageProductService;

    @Autowired
    CacheManager manager;

    @Transactional
    public void updateProductCache() {
        log.info("updating cache");
        try {
            Objects.requireNonNull(manager.getCache("products")).clear();
            List<ProductList> products = manageProductService.fetchProducts();
            log.info("got products");
            products.forEach(manageProductService::populateCache);
        } catch (Exception e) {
            log.error("Error when updating cache: {}", e.getMessage());
        }
    }
}
