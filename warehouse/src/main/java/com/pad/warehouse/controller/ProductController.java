package com.pad.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.swaggerdemo.pet.api.ProductsApi;
import com.pad.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController implements ProductsApi{

    private final ProductService productService;

}
