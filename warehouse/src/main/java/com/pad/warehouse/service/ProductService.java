package com.pad.warehouse.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.model.entity.ProductDescription;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;

import jakarta.persistence.EntityManager;
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
        return productRepository.findByQueryParams(name, productCode, quantity, price, status, type, subtype, created,
                modified);
    }

    private ProductList prepareProductList(Product product) {
        ProductList productList = new ProductList();
        try {
            com.pad.warehouse.swagger.model.Product dataProduct = productMapper.mapToDataProduct(product);
            productList.setProduct(dataProduct);
        } catch (Exception e) {
            log.error("Couldn't map product: {} to DataObject, error: {}", product, e.getMessage());
            // TODO: handle exception
        }
        List<com.pad.warehouse.swagger.model.ProductDescription> productDescriptionsForProduct = productDescriptionService.getDataProductDescriptionsForProduct(product.getId());
        productList.setProductDescription(productDescriptionsForProduct);
        return productList;
    }

    public Product getProduct(String id) {
        return productRepository.findById(Long.parseLong(id)).get();
    }

    @Transactional
    public String saveProductData(@Valid CreateProductRequest body) {
        log.info("save product: {}, START", body);
        // TODO: automap datetime

        Product savedProduct = productMapper.mapToEntityProduct(body.getProduct());
        savedProduct.setCreated(LocalDateTime.now());
        productRepository.save(savedProduct);
        productRepository.flush();
        if (body.getProductDescription() != null && !body.getProductDescription().isEmpty()) {
            body.getProductDescription().forEach(productDescription -> productDescriptionService.saveProductDescription(productDescription, savedProduct.getId()));
        }
        log.info("save product: {}, END", body);
        return String.valueOf(savedProduct.getId());
    }
}
