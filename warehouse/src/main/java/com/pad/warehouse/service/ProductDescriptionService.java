package com.pad.warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pad.warehouse.mappers.ProductDescriptionMapper;
import com.pad.warehouse.model.entity.ProductDescription;
import com.pad.warehouse.repository.ProductDescriptionRepository;

import jakarta.transaction.Transactional;
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
                com.pad.warehouse.swagger.model.ProductDescription dataProductDescription = convertEntityToData(productDescription);
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

    @Transactional
    public Long saveProductDescription(com.pad.warehouse.swagger.model.ProductDescription productDescription, Long productId) {
        log.info("Save product description: {}, for product: {}, START", productDescription, productId);
        ProductDescription productDescriptionEntity = convertDataToEntity(productDescription);
        productDescriptionEntity.setProductId(productId);
        if (validateProductDescription(productDescriptionEntity)) {
            try {
                ProductDescription savedProductDescription = productDescriptionRepository.save(productDescriptionEntity);
                productDescriptionRepository.flush();
                log.info("Save product description: {}, for product: {}, END", productDescriptionEntity, productId);
                return savedProductDescription.getId();
            } catch (Exception e) {
                log.error("Unexpected error while saving productdescription: {}, error: {}", productDescriptionEntity,
                        e.getMessage());
                return -1L;
                // TODO: handle exception
            }
        }
        log.error("ProductDescription not valid");
        // TODO: return some error list etc?
        return -1L;
    }

    private ProductDescription convertDataToEntity(
            com.pad.warehouse.swagger.model.ProductDescription productDescription) {
        try {
            return productDescriptionMapper.mapToEntityProductDescription(productDescription);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private com.pad.warehouse.swagger.model.ProductDescription convertEntityToData(ProductDescription productDescription) {
        try {
            return productDescriptionMapper.mapToDataProductDescription(productDescription);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private boolean validateProductDescription(ProductDescription productDescription) {
        log.info("Product description validation {}: START", productDescription);
        log.info("Product description validation {}: END", productDescription);
        return true;
    }

}
