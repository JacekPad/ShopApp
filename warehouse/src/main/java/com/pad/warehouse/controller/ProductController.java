package com.pad.warehouse.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pad.warehouse.swagger.api.ProductsApi;
import com.pad.warehouse.swagger.model.CreateProductDescriptionRequest;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.DeleteResponse;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductCreationResponse;
import com.pad.warehouse.swagger.model.ProductDescription;
import com.pad.warehouse.swagger.model.ProductDescriptionCreationResponse;
import com.pad.warehouse.swagger.model.ProductDescriptionsResponse;
import com.pad.warehouse.swagger.model.ProductResponse;
import com.pad.warehouse.swagger.model.ProductsResponse;
import com.pad.warehouse.swagger.model.UpdateProductDescriptionRequest;
import com.pad.warehouse.swagger.model.UpdateProductRequest;
import com.pad.warehouse.mappers.ResponseHeaderMapper;
import com.pad.warehouse.service.ProductDescriptionService;
import com.pad.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductsApi {

    private final ProductService productService;
    private final ProductDescriptionService productDescriptionService;

    @Override
    public ResponseEntity<ProductCreationResponse> addProduct(@Valid CreateProductRequest body) {
        log.info("Add product: {}: START", body);
        Long savedProductId = productService.saveProductData(body);
        ProductCreationResponse response = new ProductCreationResponse();
        response.setProductId(String.valueOf(savedProductId));
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Add product: {}: END", response.getResponseHeader());
        return new ResponseEntity<ProductCreationResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductDescriptionCreationResponse> addProductDescriptionsByProductId(String productId,
            @Valid CreateProductDescriptionRequest body) {
        log.info("Add Product Description: ID - {}, Desc - {}, START", productId, body);
        Long saveProductDescriptionId = productDescriptionService.saveProductDescription(body.getProductDescription(),
                Long.valueOf(productId));
        ProductDescriptionCreationResponse response = new ProductDescriptionCreationResponse();
        response.setProductDescriptionId(String.valueOf(saveProductDescriptionId));
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Add Product Description {}, END", response.getResponseHeader());
        return new ResponseEntity<ProductDescriptionCreationResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(String productId) {
        log.info("Get Product by Id: {}, START", productId);
        Product product = productService.getProductData(productId);
        ProductResponse response = new ProductResponse();
        response.setProduct(product);
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Get Product by Id: {}, END", product);
        return new ResponseEntity<ProductResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> getProductDescriptionsByProductId(String productId) {
        log.info("Get Product Description: {}, START", productId);
        List<ProductDescription> dataProductDescriptionsForProduct = productDescriptionService
                .getDataProductDescriptionsForProduct(Long.valueOf(productId));
        ProductDescriptionsResponse response = new ProductDescriptionsResponse();
        response.setProductDescriptions(dataProductDescriptionsForProduct);
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Get Product Description: {}, END", response.getResponseHeader());
        return new ResponseEntity<ProductDescriptionsResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductsResponse> getProducts(@Valid String name, @Valid String productCode,
            @Valid String quantity, @Valid String price, @Valid String status, @Valid String type,
            @Valid String subtype, @Valid String created, @Valid String modified) {
        log.info("Get products query, START");
        ProductsResponse productsResponse = productService.getProductsData(name, productCode, quantity, price, status,
                type,
                subtype, created, modified);
        productsResponse.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Get products query: {}, END", productsResponse.getResponseHeader());
        return new ResponseEntity<ProductsResponse>(productsResponse, null, 200);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProduct(String productId) {
        log.info("Remove Product: {}, START", productId);
        DeleteResponse response = new DeleteResponse();
        String message = productService.removeProduct(productId);
        response.setMessage(message);
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Remove Product: {}, END", response.getResponseHeader());
        return new ResponseEntity<>(response, null, 200);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProductDescriptionByProductId(String descriptionId) {
        log.info("Remove Product Description: {}, START", descriptionId);
        DeleteResponse response = new DeleteResponse();
        String removeProductDescription = productDescriptionService.removeProductDescription(Long.valueOf(descriptionId));
        response.setMessage(removeProductDescription);
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        log.info("Remove Product Description: {}, END", response.getResponseHeader());
        return new ResponseEntity<DeleteResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(String productId, @Valid UpdateProductRequest body) {
        log.info("Update Product: Id - {} Body - {}, START", productId, body);
        Product updatedProduct = productService.updateProductData(productId, body.getProduct());
        ProductResponse response = new ProductResponse();
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        response.setProduct(updatedProduct);
        log.info("Update Product: {}, END", body);
        return new ResponseEntity<ProductResponse>(response, null, 200);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> updateProductDescriptionsByProductId(String descriptionId,
            @Valid UpdateProductDescriptionRequest body) {
                // TODO kinda bad response object, change swagger?
        log.info("Update Product Description: Id - {} Body - {}, START", descriptionId, body);
        ProductDescriptionsResponse response = new ProductDescriptionsResponse();
        ProductDescription updateProductDescription = productDescriptionService.updateProductDescription(descriptionId, body.getProductDescription());
        response.setResponseHeader(ResponseHeaderMapper.createResponseHeader(UUID.randomUUID()));
        response.setProductDescriptions(List.of(updateProductDescription));
        log.info("Update Product Description, END");
        return new ResponseEntity<ProductDescriptionsResponse>(response, null, 200);
    }

}
