package com.pad.warehouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pad.warehouse.model.data.ProductData;
import com.pad.warehouse.model.data.ResultStatus;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.isPresent() ? product.get() : null;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public ResultStatus addProduct(ProductData productData) {
        


        return new ResultStatus();
    }

}
