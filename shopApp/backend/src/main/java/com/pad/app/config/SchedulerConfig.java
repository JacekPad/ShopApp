package com.pad.app.config;

import com.pad.app.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@AllArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final ProductService service;

    @Scheduled(cron = "${app-config.cache.clear-cache.fixed-timer}")
    public void updateProductCache() {
        service.updateProductCache();
    }


}
