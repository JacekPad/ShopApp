package com.pad.warehouse.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pad.warehouse.api.ProductsApi;
import com.pad.warehouse.model.CreateProductDescriptionRequest;
import com.pad.warehouse.model.CreateProductRequest;
import com.pad.warehouse.model.DeleteResponse;
import com.pad.warehouse.model.ProductCreationResponse;
import com.pad.warehouse.model.ProductDescriptionCreationResponse;
import com.pad.warehouse.model.ProductDescriptionsResponse;
import com.pad.warehouse.model.ProductResponse;
import com.pad.warehouse.model.ProductsResponse;
import com.pad.warehouse.model.UpdateProductDescriptionRequest;
import com.pad.warehouse.model.UpdateProductRequest;
import com.pad.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ProductController implements ProductsApi{

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductCreationResponse> addProduct(@Valid CreateProductRequest body) {
        System.out.println("addProduct");
        System.out.println(body);
        // TODO Auto-generated method stub
        return ProductsApi.super.addProduct(body);
    }

    @Override
    public ResponseEntity<ProductDescriptionCreationResponse> addProductDescriptionsByProductId(String productId,
            @Valid CreateProductDescriptionRequest body) {
        System.out.println("addProductDescription");
        System.out.println(body);
        // TODO Auto-generated method stub
        return ProductsApi.super.addProductDescriptionsByProductId(productId, body);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(String productId) {
        System.out.println("get product by id");
        // TODO Auto-generated method stub
        return ProductsApi.super.getProductById(productId);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> getProductDescriptionsByProductId(String productId) {
        System.out.println("getProductDesc");
        // TODO Auto-generated method stub
        return ProductsApi.super.getProductDescriptionsByProductId(productId);
    }

    @Override
    public ResponseEntity<ProductsResponse> getProducts(@Valid String id, @Valid String name, @Valid String productCode,
            @Valid String quantity, @Valid String price, @Valid String status, @Valid String type,
            @Valid String subtype, @Valid String created, @Valid String modified) {
        System.out.println("getProducts - query");
        System.out.println(id);
        System.out.println(name);
        System.out.println(productCode);
        System.out.println(quantity);
        System.out.println(price);
        System.out.println(status);
        System.out.println(type); 
        System.out.println(subtype); 
        System.out.println(created); 
        System.out.println(modified); 
        // TODO Auto-generated method stub
        return ProductsApi.super.getProducts(id, name, productCode, quantity, price, status, type, subtype, created, modified);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProduct(String productId) {
        System.out.println("remove product");
        // TODO Auto-generated method stub
        return ProductsApi.super.removeProduct(productId);
    }

    @Override
    public ResponseEntity<DeleteResponse> removeProductDescriptionByProductId(String descriptionId) {
        System.out.println("removeProductDescipriotns");
        // TODO Auto-generated method stub
        return ProductsApi.super.removeProductDescriptionByProductId(descriptionId);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(String productId, @Valid UpdateProductRequest body) {
        System.out.println("updatePRoducts");
        System.out.println(body);
        // TODO Auto-generated method stub
        return ProductsApi.super.updateProduct(productId, body);
    }

    @Override
    public ResponseEntity<ProductDescriptionsResponse> updateProductDescriptionsByProductId(String descriptionId,
            @Valid UpdateProductDescriptionRequest body) {
        System.out.println("updateProducttDescirption");
        System.out.println(body);
        // TODO Auto-generated method stub
        return ProductsApi.super.updateProductDescriptionsByProductId(descriptionId, body);
    }

}
