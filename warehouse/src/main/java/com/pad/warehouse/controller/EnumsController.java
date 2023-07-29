package com.pad.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pad.warehouse.model.entity.ProductStatus;
import com.pad.warehouse.model.entity.ProductSubtype;
import com.pad.warehouse.model.entity.ProductType;
import com.pad.warehouse.service.ProductCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class EnumsController {

    private final ProductCacheService enumsService;

    @Autowired
    private final CacheManager manager;

    @GetMapping("/status")
    public List<ProductStatus> getStatus() {
        return enumsService.getStatuses();
    }

    @GetMapping("/type")
    public List<ProductType> getType() { 
        return null;
    }

    @GetMapping("/subtype")
    public List<ProductSubtype> getSubtype() {
        return null;
    }

    @GetMapping("/clear")
    public void clearCache() {
        manager.getCacheNames().stream().forEach(name -> manager.getCache(name).clear());
    }


}

