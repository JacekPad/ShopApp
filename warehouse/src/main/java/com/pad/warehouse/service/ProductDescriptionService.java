package com.pad.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.badRequest.ValidationException;
import com.pad.warehouse.exception.conflict.ProductDescriptionExistsException;
import com.pad.warehouse.exception.internal.FetchDataError;
import com.pad.warehouse.exception.internal.SaveObjectException;
import com.pad.warehouse.exception.notFound.NoObjectFound;
import com.pad.warehouse.exception.unprocessable.ProductDescriptionMapperException;
import com.pad.warehouse.mappers.ProductDescriptionMapper;
import com.pad.warehouse.model.entity.ProductDescriptionEntity;
import com.pad.warehouse.repository.ProductDescriptionRepository;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.ProductDescription;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductDescriptionService {

    private final ProductDescriptionRepository productDescriptionRepository;
    private final ProductDescriptionMapper productDescriptionMapper;
    private final ProductRepository productRepository;

    public List<ProductDescription> getDataProductDescriptionsForProduct(
            Long productId) {
        if (!productRepository.findById(productId).isPresent()) {
            log.error("No product found for ID: {}", productId);
            throw new NoObjectFound("No product found");
        }
        List<ProductDescriptionEntity> entityProductDescriptionForProduct = getEntityProductDescriptionForProduct(
                productId);
        List<ProductDescription> dataList = new ArrayList<>();
        for (ProductDescriptionEntity productDescription : entityProductDescriptionForProduct) {
            ProductDescription dataProductDescription = convertEntityToData(
                    productDescription);
            dataList.add(dataProductDescription);
        }
        return dataList;
    }

    public List<ProductDescriptionEntity> getEntityProductDescriptionForProduct(Long productId) {
        log.info("Get descriptions for product: {}: START", productId);
        try {
            log.info("Get descriptions for product: {}: END", productId);
            return productDescriptionRepository.getByProductId(productId);
        } catch (Exception e) {
            log.error("Error while fetching product descriptions for product ID: {} - {}", productId, e.getMessage());
            // TODO save errors to DB and fetch error id and put it in exception message
            throw new FetchDataError("Error while fetching product descriptions for product ID: " + productId);
        }
    }

    @Transactional
    public Long saveProductDescription(ProductDescription productDescription,
            Long productId) {
        Map<String, String> errors = new HashMap<>();
        // TODO max number of descrpitons per product
        log.info("Save product description: {}, for product: {}, START", productDescription, productId);

        if (productDescription.getId() != null) {
            Optional<ProductDescriptionEntity> findById = productDescriptionRepository
                    .findById(Long.valueOf(productDescription.getId()));
            if (findById.isPresent()) {
                log.error("Product description: {} already exists", productDescription.getId());
                throw new ProductDescriptionExistsException("Product description already exists");
            }
        }
        if (!productRepository.findById(productId).isPresent()) {
            log.error("No product found for ID: {}", productId);
            throw new NoObjectFound("No product found");
        }
        ProductDescriptionEntity productDescriptionEntity = convertDataToEntity(productDescription);
        productDescriptionEntity.setProductId(productId);
        if (validateAddProductDescription(productDescriptionEntity, errors, productId)) {
            try {
                productDescriptionRepository.save(productDescriptionEntity);
                productDescriptionRepository.flush();
                log.info("Save product description: {}, for product: {}, END", productDescriptionEntity, productId);
                return productDescriptionEntity.getId();
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

    private ProductDescriptionEntity convertDataToEntity(
            ProductDescription productDescription) {
        try {
            return productDescriptionMapper.mapToEntityProductDescription(productDescription);
        } catch (Exception e) {
            log.error("Mapping data to entity failed: {}", e.getMessage());
            throw new ProductDescriptionMapperException("Mapping data to entity failed: " + e.getMessage());
        }
    }

    private ProductDescription convertEntityToData(
            ProductDescriptionEntity productDescription) {
        try {
            return productDescriptionMapper.mapToDataProductDescription(productDescription);
        } catch (Exception e) {
            log.error("Mapping entity to data failed: {}", e.getMessage());
            throw new ProductDescriptionMapperException("Mapping entity to data failed: " + e.getMessage());
        }
    }

    private boolean validateAddProductDescription(ProductDescriptionEntity productDescription,
            Map<String, String> errors, Long productId) {
        if (productId == null)
            errors.put("ProductId", "Product ID cannot be empty");
        log.info("Product description validation {}: START", productDescription);
        log.info("Product description validation {}: END", productDescription);
        return errors.isEmpty() ? true : false;
    }

    @Transactional
    public void removeProductDescription(Long productDescriptionId) {
        Optional<ProductDescriptionEntity> productDescription = productDescriptionRepository
                .findById(productDescriptionId);
        if (productDescription.isPresent()) {
            productDescriptionRepository.delete(productDescription.get());
        } else {
            log.error("No product description for ID: {}", productDescriptionId);
            throw new NoObjectFound("No product description found");
        }
    }

    public void updateProductDescription(String descriptionId, ProductDescription productDescription) {
        if (descriptionId != null) {
            Optional<ProductDescriptionEntity> productDescriptionEntity = productDescriptionRepository
                    .findById(Long.valueOf(descriptionId));
            if (!productDescriptionEntity.isPresent()) {
                log.error("Product description with id {} does not exists", descriptionId);
                throw new NoObjectFound("Product description does not exists");
            }
            ProductDescriptionEntity productDescriptionEntityToUpdate = productDescriptionMapper
                    .mapToEntityProductDescription(productDescription);
            productDescriptionEntityToUpdate.setId(Long.valueOf(descriptionId));
            try {
                productDescriptionRepository.save(productDescriptionEntityToUpdate);
                productDescriptionRepository.flush();
            } catch (Exception e) {
                log.error("Unexpected error while updating product: {}, error: {}", descriptionId,
                        e.getMessage());
                throw new SaveObjectException("Unexpected error while updating product description");
            }
        } else
            throw new SaveObjectException("Product description id could not be specified");
    }

}
