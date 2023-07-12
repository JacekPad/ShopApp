package com.pad.warehouse.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.conflict.ProductExistsException;
import com.pad.warehouse.exception.internal.FetchDataError;
import com.pad.warehouse.exception.internal.SaveObjectException;
import com.pad.warehouse.exception.unprocessable.ProductMapperException;
import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.model.entity.ProductDescription;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDescriptionService productDescriptionService;
    private final ProductMapper productMapper;

    public ProductsResponse getProductsResponse(@Valid String name, @Valid String productCode, @Valid String quantity,
            @Valid String price, @Valid String status, @Valid String type, @Valid String subtype, @Valid String created,
            @Valid String modified) {
        log.info("Get product response: START");
        ProductsResponse response = new ProductsResponse();
        List<Product> products = getProducts(name, productCode, quantity, price, status, type, subtype, created,
                modified);
        for (Product product : products) {
            response.addProductsItem(prepareProductList(product));
        }
        log.info("Get product response: END");
        return response;
    }

    private List<Product> getProducts(String name, String productCode,
            String quantity, String price, String status, String type,
            String subtype, String created, String modified) {
                log.info("Get products: START");
                try {
                    log.info("Get products: END");
                    return productRepository.findByQueryParams(name, productCode, quantity, price, status, type, subtype, created,
                    modified);
                } catch (Exception e) {
                    log.error("Error while fetching products: {}", e.getMessage());
                    throw new FetchDataError("Error while fetching products: " + e.getMessage());
                }
    }

    private ProductList prepareProductList(Product product) {
        ProductList productList = new ProductList();
        com.pad.warehouse.swagger.model.Product dataProduct = convertEntityToData(product);
        productList.setProduct(dataProduct);
        List<com.pad.warehouse.swagger.model.ProductDescription> productDescriptionsForProduct = productDescriptionService
                .getDataProductDescriptionsForProduct(product.getId());
        productList.setProductDescription(productDescriptionsForProduct);
        return productList;
    }

    public Product getProductEntity(String id) {
        return productRepository.findById(Long.parseLong(id)).get();
    }

    public com.pad.warehouse.swagger.model.Product getProductData(String id) {
        Product productEntity = getProductEntity(id);
        return convertEntityToData(productEntity);
    }

    private Product convertDataToEntity(com.pad.warehouse.swagger.model.Product productData) {
        try {
            return productMapper.mapToEntityProduct(productData);
        } catch (Exception e) {
            log.error("Mapping data to entity failed: {}", e.getMessage());
            throw new ProductMapperException("Mapping data to entity failed: " + e.getMessage());
        }
    }

    private com.pad.warehouse.swagger.model.Product convertEntityToData(Product productEntity) {
        try {
            return productMapper.mapToDataProduct(productEntity);
        } catch (Exception e) {
            log.error("Mapping entity to data failed: {}", e.getMessage());
            throw new ProductMapperException("Mapping entity to data failed: " + e.getMessage());
        }
    }

    @Transactional
    public Long saveProductData(@Valid CreateProductRequest body) {
        log.info("save product - BODY: {}, START", body);
        // TODO: automap datetime
        if (body.getProduct().getId() != null) {
            Optional<Product> findById = productRepository.findById(Long.valueOf(body.getProduct().getId()));
            if (findById.isPresent()) {
                log.error("product: {} already exists", body.getProduct().getId());
                throw new ProductExistsException("Product already exists");
            }
        }
        Product productEntityToSave = convertDataToEntity(body.getProduct());
        try {
            productRepository.save(productEntityToSave);
        } catch (Exception e) {
            log.error("Unexpected error while saving product: {}, error: {}", productEntityToSave,
                e.getMessage());
            throw new SaveObjectException("Unexpected error while saving: " + e.getMessage());
        }
        productRepository.flush();
        if (body.getProductDescription() != null && !body.getProductDescription().isEmpty()) {
            body.getProductDescription().forEach(productDescription -> productDescriptionService
                    .saveProductDescription(productDescription, productEntityToSave.getId()));
        }
        log.info("save product - ID: {}, END", productEntityToSave.getId());
        return productEntityToSave.getId();
    }

    @Transactional
    public String removeProduct(String productId) {
        Optional<Product> product = productRepository.findById(Long.valueOf(productId));
        try {
            if (product.isPresent()) {
                List<ProductDescription> productDescriptions = productDescriptionService
                        .getEntityProductDescriptionForProduct(product.get().getId());
                productDescriptions.forEach(productDescription -> {
                    productDescriptionService.removeProductDescription(productDescription.getId());
                });
                productRepository.delete(product.get());
                return "Product successfully removed";
            }
            // TODO exception
            return "No product";
        } catch (Exception e) {
            // TODO: handle exception
            return "error";
        }

    }
}
