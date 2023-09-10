package com.pad.app.service;

import com.pad.warehouse.swagger.model.ProductList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class SchedulerService {

    private final ManageProductService manageProductService;

    @Autowired
    CacheManager manager;

    public void updateProductCache() {
        log.info("updating cache");
        List<ProductList> tempProducts = manageProductService.getProducts();
        try {
            Objects.requireNonNull(manager.getCache("products")).clear();
            List<ProductList> products = manageProductService.getProducts();
        } catch (Exception e) {
            log.error("Error when updating cache: {}", e.getMessage());
            List<ProductList> productLists = manageProductService.restoreBackupCache(tempProducts);
        }
    }
}
