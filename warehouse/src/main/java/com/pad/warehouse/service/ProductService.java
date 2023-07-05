package com.pad.warehouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

}
