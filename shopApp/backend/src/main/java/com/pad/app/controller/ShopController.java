package com.pad.app.controller;

import com.pad.app.model.Order;
import com.pad.app.model.Product;
import com.pad.app.model.ProductOrder;
import com.pad.app.model.enums.DeliveryMethodEnum;
import com.pad.app.service.OrderService;
import com.pad.app.service.ProductService;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Product product = new Product();
        product.setId(1L);
        product.setName("123");
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(product);
        productOrder.setQuantityBought(4);
        Product product1 = new Product();
        product1.setId(4L);
        ProductOrder productOrder1 = new ProductOrder();
        productOrder1.setProduct(product1);
        productOrder1.setQuantityBought(99);
        List<ProductOrder> orders = new ArrayList<>();
        orders.add(productOrder);
        orders.add(productOrder1);
        Order order1 = new Order();
        order1.setProducts(orders);
        order1.setDeliveryMethod(DeliveryMethodEnum.POST);
        orderService.makeOrder(order1);
// make order and descrease number of products in warehouse and send order to other service
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductList>> getProducts() {
// get cached products or make call to warehouse to check if ok
        log.info("getting products");
        List<ProductList> products = productService.getProducts();
        return new ResponseEntity<>(products, null, 200);
    }

    @GetMapping("/products/{id}")
    public void getDetails() {
// get all product details when user clicks on product?
    }

}
