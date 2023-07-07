package com.pad.warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pad.warehouse.mappers.ProductDescriptionMapper;
import com.pad.warehouse.model.entity.ProductDescription;
import com.pad.warehouse.repository.ProductDescriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductDescriptionService {

    private final ProductDescriptionRepository productDescriptionRepository;
    private final ProductDescriptionMapper productDescriptionMapper;

    public List<com.pad.warehouse.swagger.model.ProductDescription> getDataProductDescriptionsForProduct(
            Long productId) {
        List<ProductDescription> entityProductDescriptionForProduct = getEntityProductDescriptionForProduct(productId);
        List<com.pad.warehouse.swagger.model.ProductDescription> dataList = new ArrayList<>();
        for (ProductDescription productDescription : entityProductDescriptionForProduct) {
            try {
                com.pad.warehouse.swagger.model.ProductDescription dataProductDescription = productDescriptionMapper
                        .mapToDataProductDescription(productDescription);
                dataList.add(dataProductDescription);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return dataList;
    }

    public List<ProductDescription> getEntityProductDescriptionForProduct(Long productId) {
        log.info("Get descriptions for product: {}: START", productId);
        try {
            log.info("Get descriptions for product: {}: END", productId);
            return productDescriptionRepository.getByProductId(productId);
        } catch (Exception e) {
            // TODO: handle exception
            log.info("No product found for id: {}", productId);
            return null;
        }
    }

    public ProductDescription getProductsDescription(Long descriptionId) {
        return null;
    }

}
