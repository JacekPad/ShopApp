package com.pad.warehouse.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pad.warehouse.service.ProductCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class EnumsController {

    private final ProductCacheService cacheService;
    
    @Autowired
    private final CacheManager manager;

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return cacheService.getStatusMap();
    }

    @GetMapping("/type")
    public Map<String, String> getType() { 
        return cacheService.getTypeMap();
    }

    @GetMapping("/subtype")
    public Map<String, String> getSubtype() {
        return cacheService.getSubtypeMap();
    }

    @GetMapping("/clear")
    public void clearCache() {
        manager.getCacheNames().stream().forEach(name -> manager.getCache(name).clear());
    }


}

