package com.pad.warehouse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.badRequest.ValidationException;
import com.pad.warehouse.exception.conflict.ProductExistsException;
import com.pad.warehouse.exception.internal.FetchDataError;
import com.pad.warehouse.exception.internal.SaveObjectException;
import com.pad.warehouse.exception.notFound.NoObjectFound;
import com.pad.warehouse.exception.unprocessable.ProductMapperException;
import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.ProductEntity;
import com.pad.warehouse.model.enums.ProductLogType;
import com.pad.warehouse.model.entity.ProductDescriptionEntity;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductDescription;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;
import com.pad.warehouse.utils.DataValidators;

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
    private final DataValidators productValidator;
    private final LogService logService;

    public ProductsResponse getProductsData(@Valid String name, @Valid String productCode, @Valid String quantity,
            @Valid String price, @Valid String status, @Valid String type, @Valid String subtype, @Valid String created,
            @Valid String modified) {
        log.info("Get product response START");
        ProductsResponse response = new ProductsResponse();
        List<ProductEntity> products = getProductsEntity(name, productCode, quantity, price, status, type, subtype,
                created,
                modified);
        if (products.isEmpty()) {
            throw new NoObjectFound("No products with given attributes");
        }
        for (ProductEntity product : products) {
            response.addProductsItem(prepareProductList(product));
        }
        log.info("Get product response END");
        return response;
    }

    private List<ProductEntity> getProductsEntity(String name, String productCode,
            String quantity, String price, String status, String type,
            String subtype, String created, String modified) {
        log.info("Get products START");
        try {
            log.info("Get products END");
            return productRepository.findByQueryParams(name, productCode, quantity, price, status, type, subtype,
                    created,
                    modified);
        } catch (Exception e) {
            log.error("Error while fetching products: {}", e.getMessage());
            throw new FetchDataError("Error while fetching products");
        }
    }

    private ProductList prepareProductList(ProductEntity product) {
        ProductList productList = new ProductList();
        Product dataProduct = convertEntityToData(product);
        productList.setProduct(dataProduct);
        List<ProductDescription> productDescriptionsForProduct = productDescriptionService
                .getDataProductDescriptionsForProduct(product.getId());
        productList.setProductDescription(productDescriptionsForProduct);
        return productList;
    }

    public ProductEntity getProductEntity(Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent())
            return product.get();
        else {
            log.error("No product found for ID: {}", id);
            throw new NoObjectFound("No product found");
        }
    }

    public Product getProductData(String id) {
        ProductEntity productEntity = getProductEntity(Long.parseLong(id));
        return convertEntityToData(productEntity);
    }

    private ProductEntity convertDataToEntity(Product productData) {
        try {
            return productMapper.mapToEntityProduct(productData);
        } catch (Exception e) {
            log.error("Mapping data to entity failed: {}", e.getMessage());
            throw new ProductMapperException("Mapping data to entity failed");
        }
    }

    private Product convertEntityToData(ProductEntity productEntity) {
        try {
            return productMapper.mapToDataProduct(productEntity);
        } catch (Exception e) {
            log.error("Mapping entity to data failed: {}", e.getMessage());
            throw new ProductMapperException("Mapping entity to data failed");
        }
    }

    @Transactional
    public Long saveProductData(@Valid CreateProductRequest body) {
        log.info("save product - BODY: {}, START", body);
        Product product = body.getProduct();
        Map<String, String> errors = new HashMap<>();
        if (product.getId() != null) {
            Optional<ProductEntity> findById = productRepository.findById(Long.valueOf(product.getId()));
            if (findById.isPresent()) {
                log.error("product: {} already exists", product.getId());
                throw new ProductExistsException("Product already exists");
            }
        }

        if (!productValidator.validateProduct(product, errors, true)) {
            log.error("Validation errors for add Product {} - errors: {}", product, errors);
            throw new ValidationException(errors);
        }

        ProductEntity productEntityToSave = convertDataToEntity(product);
        try {
            productRepository.saveAndFlush(productEntityToSave);
        } catch (Exception e) {
            log.error("Unexpected error while saving product: {}, error: {}", productEntityToSave,
                    e.getMessage());
            throw new SaveObjectException("Unexpected error while saving product");
        }
        if (body.getProductDescription() != null && !body.getProductDescription().isEmpty()) {
            body.getProductDescription().forEach(productDescription -> productDescriptionService
                    .saveProductDescription(productDescription, productEntityToSave.getId()));
        }
        logService.saveToLog(productEntityToSave.getId(), ProductLogType.PRODUCT, "Product saved");
        log.info("save product - ID: {}, END", productEntityToSave.getId());
        return productEntityToSave.getId();
    }

    @Transactional
    public String removeProduct(String productId) {
        log.info("remove product: {} START", productId);
        Optional<ProductEntity> product = productRepository.findById(Long.valueOf(productId));
        if (product.isPresent()) {
            List<ProductDescriptionEntity> productDescriptions = productDescriptionService
                    .getEntityProductDescriptionForProduct(product.get().getId());
            productDescriptions.forEach(productDescription -> {
                productDescriptionService.removeProductDescription(productDescription.getId());
            });
            productRepository.delete(product.get());
            log.info("remove product: {} END", productId);
            logService.saveToLog(product.get().getId(), ProductLogType.PRODUCT, "Product deleted");
            return "Product removed successfully";
        } else {
            log.error("No product found for ID: {}", productId);
            throw new NoObjectFound("No product found");
        }
    }

    @Transactional
    public Product updateProductData(String productId, Product product) {
        log.info("update product - BODY: {}, START", product);
        Map<String, String> errors = new HashMap<>();
        if (productId != null) {
            Optional<ProductEntity> productEntity = productRepository.findById(Long.valueOf(productId));
            if (productEntity.isEmpty()) {
                log.error("Product with id {} does not exists", productId);
                throw new NoObjectFound("Product does not exists");
            }

            if (!productValidator.validateProduct(product, errors, false)) {
            log.error("Validation errors for update Product {} - errors: {}", product, errors);
            throw new ValidationException(errors);
            }
//            TODO make mapper (map struct)?
            ProductEntity productEntityToUpdate = productEntity.get();
            if (product.getName() != null) productEntityToUpdate.setName(product.getName());
            if (product.getProductCode() != null) productEntityToUpdate.setProductCode(product.getProductCode());
            if (product.getQuantity() != null) productEntityToUpdate.setQuantity(Integer.parseInt(product.getQuantity()));
            if (product.getPrice() != null) productEntityToUpdate.setPrice(Long.parseLong(product.getPrice()));
            if (product.getStatus() != null) productEntityToUpdate.setStatus(product.getStatus());
            if (product.getType() != null) productEntityToUpdate.setType(product.getType());
            if (product.getSubtype() != null) productEntityToUpdate.setSubtype(product.getSubtype());
            try {
                productRepository.saveAndFlush(productEntityToUpdate);
            } catch (Exception e) {
                log.error("Unexpected error while updating product: {}, error: {}", productId,
                        e.getMessage());
                throw new SaveObjectException("Unexpected error while updating product");
            }
            log.info("update product: {}, END", productEntityToUpdate);
            logService.saveToLog(productEntityToUpdate.getId(), ProductLogType.PRODUCT, "Product updated");
            return productMapper.mapToDataProduct(productEntityToUpdate);
        } else {
            log.error("Product id could not be specified: ID {}", productId);
            throw new SaveObjectException("Product id could not be specified");
        }
    }

    public void changeProductQuantity(Long productId, int quantityChange) {
//       TODO return message 200 OK, product updated?
//        or some confirmation to the broker?
//        and some tests
        Optional<ProductEntity> product = productRepository.findById(productId);
        if (product.isPresent()) {
            ProductEntity productEntity = product.get();
            int quantity = productEntity.getQuantity();
            if (quantityChange > 0) {
//            order canceled, restore product quantity
                quantity += quantityChange;
                productEntity.setQuantity(quantity);
                productRepository.saveAndFlush(productEntity);
            } else if (quantityChange < 0) {
//            new order, decrease product quantity
                if (quantity - quantityChange <= 0) {
                    quantity -= quantityChange;
                    productEntity.setQuantity(quantity);
                    productRepository.saveAndFlush(productEntity);
                } else {
//                    TODO some error
//                    not enough products left
                }
            }

        } else {
            log.error("Product with id {} does not exists", productId);
            throw new NoObjectFound("Product does not exists");
        }
    }


}
