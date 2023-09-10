package com.pad.app.config;

import com.pad.app.service.ManageProductService;
import com.pad.app.service.SchedulerService;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@AllArgsConstructor
@Slf4j
public class SchedulerConfig {

    @Autowired
    private final CacheManager manager;

    private final SchedulerService service;

    @Scheduled(cron = "${app-config.cache.clear-cache.fixed-timer}")
    public void updateProductCache() {
        service.updateProductCache();
    }


}
