package com.pad.app.service;

import org.springframework.stereotype.Service;

import com.pad.app.model.Product;

@Service
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

    public boolean isProductAvailable(Product product) {
        // check if available and update cached value?
        updateProductAvailability(product);
        return false;
    }

    private void updateProductAvailability(Product productToCheck) {
        // update cached quantity value of checked item? (is it possible?)
    }


    
}
