package com.pad.app.controller;

import com.pad.app.model.Order;
import com.pad.app.service.OrderService;
import com.pad.app.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;


    @PostMapping("/order")
    public void makeOrder(@RequestBody Order order) {
        orderService.makeOrder(order);
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
