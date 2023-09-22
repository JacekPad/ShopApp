package com.pad.app.controller;

import com.pad.app.model.Order;
import com.pad.app.model.enums.DeliveryMethodEnum;
import com.pad.app.service.OrderService;
import com.pad.app.service.ProductService;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;

    private final ProductService productService;

//    @PostMapping("/order")
    @GetMapping("/order")
//    @RequestBody Order order
    public void makeOrder() {
        log.info("makeOrder - Controller - START");
        Order order1 = new Order();
        order1.setProducts(null);
        order1.setDeliveryMethod(DeliveryMethodEnum.POST);
        orderService.makeOrder(order1);
        log.info("makeOrder - Controller - END");
// make order and descrease number of products in warehouse and send order to other service
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductList>> getProducts() {
// get cached products or make call to warehouse to check if ok
        log.info("getProducts - Controller - START");
        List<ProductList> products = productService.getProducts();
        log.info("getProducts - Controller - END - {}", products);
        return new ResponseEntity<>(products, null, 200);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductList> getDetails(@PathVariable String id) {
        log.info("get Details - Controller - START");
        ProductList productDetails = productService.getProductDetails(id);
        log.info("get Details - Controller - END - {}", productDetails);
        return new ResponseEntity<>(productDetails, null, 200);
// get all product details when user clicks on product?
    }

}
