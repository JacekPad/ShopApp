package com.pad.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.pad.app.model.Product;

@Service
@Slf4j
public class ProductService {
    
    public void getProducts() {
        // get product from warehouse and cache it?
    }

    public void getProductDetails() {
        // get details of product
    }

    public void cleanCache() {
        // clean cache with scheduler
    }

    public boolean isProductAvailable(Long id) {
        // check if available and update cached value?
//        TODO maybe send with rabbitMQ all product as multithread and wait for each response?
//        updateProductAvailability(id);
        return true;
    }

    private void updateProductAvailability(Long id) {
        // update cached quantity value of checked item? (is it possible?)
    }
}
