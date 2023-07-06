package com.pad.warehouse.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pad.warehouse.swagger.api.ProductsApi;
import com.pad.warehouse.swagger.model.CreateProductDescriptionRequest;
import com.pad.warehouse.swagger.model.CreateProductRequest;
import com.pad.warehouse.swagger.model.DeleteResponse;
import com.pad.warehouse.swagger.model.ProductCreationResponse;
import com.pad.warehouse.swagger.model.ProductDescriptionCreationResponse;
import com.pad.warehouse.swagger.model.ProductDescriptionsResponse;
import com.pad.warehouse.swagger.model.ProductResponse;
import com.pad.warehouse.swagger.model.ProductsResponse;
import com.pad.warehouse.swagger.model.UpdateProductDescriptionRequest;
import com.pad.warehouse.swagger.model.UpdateProductRequest;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductCreationResponse> addProduct(@Valid CreateProductRequest body) {
        log.info("Add product: {}: START", body);
        log.info("Add product: {}: END", body);
        return ProductsApi.super.addProduct(body);
    }

    @Override
    public ResponseEntity<ProductDescriptionCreationResponse> addProductDescriptionsByProductId(String productId,
            @Valid CreateProductDescriptionRequest body) {
        log.info("Add Product Description: ID - {}, Desc - {}: START", productId, body);
        log.info("Add Product Description: END");
        return ProductsApi.super.addProductDescriptionsByProductId(productId, body);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(String productId) {
        log.info("Get Product by Id: {}: START", productId);
        Product product = productService.getProduct(productId);
        log.info("Get Product by Id: {}: END", product);
        return ProductsApi.super.getProductById(productId);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> getProductDescriptionsByProductId(String productId) {
        log.info("Get Product Description: {}: START", productId);
        log.info("Get Product Description: {}: END", productId);
        return ProductsApi.super.getProductDescriptionsByProductId(productId);
    }

    @Override
    public ResponseEntity<ProductsResponse> getProducts(@Valid String name, @Valid String productCode,
            @Valid String quantity, @Valid String price, @Valid String status, @Valid String type,
            @Valid String subtype, @Valid String created, @Valid String modified) {
        log.info("Get products query: START");
        ProductsResponse productsResponse = productService.getProductsResponse(name, productCode, quantity, price, status, type,
                subtype, created, modified);
        log.info("Get products query: {}: END", productsResponse);
        return new ResponseEntity<ProductsResponse>(productsResponse, null, 200);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProduct(String productId) {
        log.info("Remove Product: {}: START", productId);
        log.info("Remove Product: {}: END", productId);
        return ProductsApi.super.removeProduct(productId);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProductDescriptionByProductId(String descriptionId) {
        log.info("Remove Product Description: {}: START", descriptionId);
        log.info("Remove Product Description: {}: END", descriptionId);
        return ProductsApi.super.removeProductDescriptionByProductId(descriptionId);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(String productId, @Valid UpdateProductRequest body) {
        log.info("Update Product: Id - {} Body - {}: START", productId, body);
        log.info("Update Product: {}: END", body);
        return ProductsApi.super.updateProduct(productId, body);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> updateProductDescriptionsByProductId(String descriptionId,
            @Valid UpdateProductDescriptionRequest body) {
        log.info("Update Product Description: Id - {} Body - {}: START", descriptionId, body);
        log.info("Update Product Description: END");
        return ProductsApi.super.updateProductDescriptionsByProductId(descriptionId, body);
    }

}
