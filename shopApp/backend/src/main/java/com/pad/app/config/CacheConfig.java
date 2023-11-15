package com.pad.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache productsCache = new CaffeineCache("products",
                Caffeine.newBuilder().build());
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCacheNames(Collections.singletonList("products"));
        return manager;
    }
}
