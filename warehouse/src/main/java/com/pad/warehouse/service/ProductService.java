package com.pad.warehouse.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.model.entity.ProductDescription;
import com.pad.warehouse.repository.ProductDescriptionRepository;
import com.pad.warehouse.repository.ProductRepository;
import com.pad.warehouse.swagger.model.ProductDescriptions;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductResponse;
import com.pad.warehouse.swagger.model.ProductsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDescriptionRepository productDescriptionRepository;
    private final ProductMapper productMapper;

    public ProductsResponse getProductsResponse(@Valid String name, @Valid String productCode, @Valid String quantity,
            @Valid String price, @Valid String status, @Valid String type, @Valid String subtype, @Valid String created,
            @Valid String modified) {
            ProductsResponse response = new ProductsResponse();
            List<Product> products = getProducts(name, productCode, quantity, price, status, type, subtype, created, modified);
            // TODO make private methods + productDesc service + mapper proddesc entity -> prodDesc Data
            for (Product product : products) {
                ProductList productObject = new ProductList();
                // List<ProductDescription> descList = productDescriptionRepository.getByProductId(product);
                com.pad.warehouse.swagger.model.Product dataProduct = productMapper.mapToDataProduct(product);
                productObject.setProduct(dataProduct);
                // if (descriptions != null) {
                //     productObject.setProductDescription(descriptions);
                // }
                response.addProductsItem(productObject);
            }
        return response;
    }

    private List<Product> getProducts(String name, String productCode,
            String quantity, String price, String status, String type,
            String subtype, String created, String modified) {
        return productRepository.findByQueryParams(name, productCode, quantity, price, status, type, subtype, created, modified);
    }

    public Product getProduct(String id) {
        return productRepository.findById(Long.parseLong(id)).get();
    }

}
