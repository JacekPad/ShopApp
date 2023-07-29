package com.pad.warehouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pad.warehouse.service.ProductCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheScheduler {

    @Autowired
    private final CacheManager cacheManager;

    private final ProductCacheService productCacheService;

    @Scheduled(cron = "${app-config.cache.clear-cache.fixed-timer}")
    public void clearAllCache() {
        log.info("Clearing all cache");
        cacheManager.getCacheNames().stream().forEach(cacheName -> productCacheService.clearCache(cacheName));
    }
    
}
