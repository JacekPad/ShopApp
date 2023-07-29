package com.pad.warehouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pad.warehouse.model.entity.ProductStatus;
import com.pad.warehouse.model.entity.ProductSubtype;
import com.pad.warehouse.model.entity.ProductType;
import com.pad.warehouse.repository.ProductStatusRepository;
import com.pad.warehouse.repository.ProductSubtypeRepository;
import com.pad.warehouse.repository.ProductTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private final ProductStatusRepository statusRepository;

    private final ProductTypeRepository typeRepository;

    private final ProductSubtypeRepository subtypeRepository;

    @Autowired
    private final CacheManager cacheManager;

    @Cacheable(value = "statuses")
    public List<ProductStatus> getStatuses() {
        return statusRepository.findAll();
    }

    @Cacheable(value = "types")
    public List<ProductType> getTypes() {
        return typeRepository.findAll();
    }

    @Cacheable(value = "subtypes")
    public List<ProductSubtype> getSubtypes() {
        return subtypeRepository.findAll();
    }

    public void clearCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

}
