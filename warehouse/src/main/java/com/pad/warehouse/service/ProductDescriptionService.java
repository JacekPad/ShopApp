package com.pad.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.badRequest.ValidationException;
import com.pad.warehouse.exception.conflict.ProductDescriptionExistsException;
import com.pad.warehouse.exception.internal.FetchDataError;
import com.pad.warehouse.exception.internal.SaveObjectException;
import com.pad.warehouse.exception.notFound.NoObjectFound;
import com.pad.warehouse.exception.unprocessable.ProductDescriptionMapperException;
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
            com.pad.warehouse.swagger.model.ProductDescription dataProductDescription = convertEntityToData(
                    productDescription);
            dataList.add(dataProductDescription);
        }
        return dataList;
    }

    public List<ProductDescription> getEntityProductDescriptionForProduct(Long productId) {
        log.info("Get descriptions for product: {}: START", productId);
        try {
            log.info("Get descriptions for product: {}: END", productId);
            return productDescriptionRepository.getByProductId(productId);
        } catch (Exception e) {
            log.error("Error while fetching product descriptions for product ID: {} - {}", productId, e.getMessage());
            throw new FetchDataError("Error while fetching product descriptions for product ID: " + productId + "error: " + e.getMessage());
        }
    }

    public ProductDescription getProductsDescription(Long descriptionId) {
        return null;
    }

    @Transactional
    public Long saveProductDescription(com.pad.warehouse.swagger.model.ProductDescription productDescription,
            Long productId) {
        Map<String, String> errors = new HashMap<>();
        // TODO max number of descrpitons per product
        log.info("Save product description: {}, for product: {}, START", productDescription, productId);
        if (productDescription.getId() != null) {
            Optional<ProductDescription> findById = productDescriptionRepository
                    .findById(Long.valueOf(productDescription.getId()));
            if (findById.isPresent()) {
                log.error("Product description: {} already exists", productDescription.getId());
                throw new ProductDescriptionExistsException("Product description already exists");
            }
        }
        ProductDescription productDescriptionEntity = convertDataToEntity(productDescription);
        productDescriptionEntity.setProductId(productId);
        if (validateProductDescription(productDescriptionEntity, errors)) {
            try {
                ProductDescription savedProductDescription = productDescriptionRepository
                        .save(productDescriptionEntity);
                productDescriptionRepository.flush();
                log.info("Save product description: {}, for product: {}, END", productDescriptionEntity, productId);
                return savedProductDescription.getId();
            } catch (Exception e) {
                log.error("Unexpected error while saving product description: {}, error: {}", productDescriptionEntity,
                        e.getMessage());
                throw new SaveObjectException("Unexpected error while saving: " + e.getMessage());
            }
        } else {
            log.error("Validation errors for description: {} and product {}", productDescriptionEntity, productId);
            throw new ValidationException(errors);
        }
    }

    private ProductDescription convertDataToEntity(
            com.pad.warehouse.swagger.model.ProductDescription productDescription) {
        try {
            return productDescriptionMapper.mapToEntityProductDescription(productDescription);
        } catch (Exception e) {
            log.error("Mapping data to entity failed: {}", e.getMessage());
            throw new ProductDescriptionMapperException("Mapping data to entity failed: " + e.getMessage());
        }
    }

    private com.pad.warehouse.swagger.model.ProductDescription convertEntityToData(
            ProductDescription productDescription) {
        try {
            return productDescriptionMapper.mapToDataProductDescription(productDescription);
        } catch (Exception e) {
            log.error("Mapping entity to data failed: {}", e.getMessage());
            throw new ProductDescriptionMapperException("Mapping entity to data failed: " + e.getMessage());
        }
    }

    private boolean validateProductDescription(ProductDescription productDescription, Map<String, String> errors) {
        log.info("Product description validation {}: START", productDescription);
        log.info("Product description validation {}: END", productDescription);
        return errors.isEmpty() ? true : false;
    }

    @Transactional
    public void removeProductDescription(Long productDescriptionId) {
        Optional<ProductDescription> productDescription = productDescriptionRepository.findById(productDescriptionId);
        try {
            if (productDescription.isPresent()) {
                productDescriptionRepository.delete(productDescription.get());
            } else {
                log.error("No product description for ID: {}", productDescriptionId);
                throw new NoObjectFound("No product description for ID: " + productDescriptionId);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
