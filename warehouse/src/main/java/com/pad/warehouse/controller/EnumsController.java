package com.pad.warehouse.controller;

import java.util.Iterator;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
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
@Slf4j
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

    @GetMapping("/test")
    public void testAuth(HttpServletRequest request) {
        log.info("START");
        log.info("header");
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        log.info("headers?: {}", iterator.hasNext());
        iterator.forEachRemaining(x -> {
            log.info(x);
            log.info(request.getHeader(x));
            log.info("================");
        });
        log.info("authType");
        log.info(request.getAuthType());
        log.info("content type");
        log.info(request.getContentType());
        log.info("testing request: {}", request);
    }
}

