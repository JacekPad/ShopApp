package com.pad.warehouse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.internal.FetchDataError;
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
    public Map<String, String> getStatusMap() {
        Map<String, String> statusMap = new HashMap<>();
        List<ProductStatus> statusList = statusRepository.findAll();
        statusList.forEach(status -> {
            if (statusMap.containsKey(status.getCode())) throw new FetchDataError("cannot have duplicate status entries");
            statusMap.put(status.getCode(), status.getValue());
        });
        return statusMap;
    }

    @Cacheable(value = "types")
    public Map<String, String> getTypeMap() {
        Map<String, String> typeMap = new HashMap<>();
        List<ProductType> typeList = typeRepository.findAll();
        typeList.forEach(type -> {
            if (typeMap.containsKey(type.getCode())) throw new FetchDataError("cannot have duplicate type entries");
            typeMap.put(type.getCode(), type.getValue());
        });
        return typeMap;
    }

    @Cacheable(value = "subtypes")
    public Map<String, String> getSubtypeMap() {
        Map<String, String> subtypeMap = new HashMap<>();
        List<ProductSubtype> subtypeList = subtypeRepository.findAll();
        subtypeList.forEach(subtype -> {
            if (subtypeMap.containsKey(subtype.getCode())) throw new FetchDataError("cannot have duplicate subtype entries");
            subtypeMap.put(subtype.getCode(), subtype.getValue());
        });
        return subtypeMap;
    }

    public void clearCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

}
