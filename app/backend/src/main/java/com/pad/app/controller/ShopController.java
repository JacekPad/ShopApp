package com.pad.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class ShopController {
    
    @PostMapping("/order")
    public void makeOrder() {
// make order and descrease number of products in warehouse and send order to other service
    }

    @GetMapping("/products")
    public void getProducts() {
// get cached products or make call to warehouse
    }

    @GetMapping("/products/{id}")
    public void getDetails() {
// get all product details when user clicks on product?
    }




}
