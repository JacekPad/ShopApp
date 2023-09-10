package com.pad.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache productsCache = new CaffeineCache("products",
                Caffeine.newBuilder().build());
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Collections.singletonList(productsCache));
        return manager;
    }

}
