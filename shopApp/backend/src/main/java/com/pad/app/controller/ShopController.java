package com.pad.app.controller;

import com.pad.app.model.ProductFilterParams;
import com.pad.app.service.OrderService;
import com.pad.app.service.ProductService;
import com.pad.app.swagger.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;

    private final ProductService productService;


    @PostMapping("/order")
    public void makeOrder(@RequestBody Order order) {
        log.info("makeOrder - Controller - START");
        orderService.makeOrder(order);
        log.info("makeOrder - Controller - END");
    }

    @GetMapping("/orders")
    private List<Order> getOrders(OrderFilterParams params) {
        log.info("getOrders - Controller - START, params: {}", params);
        List<Order> orders = orderService.getOrders(params);
        log.info("getOrders - Controller - END");
        return orders;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(ProductFilterParams params, HttpServletRequest request, HttpServletResponse response) {
        log.info("getProducts - Controller - START");
        List<Product> products = productService.getProducts(params);
        log.info("getProducts - Controller - END - {}", products);
        return new ResponseEntity<>(products, null, 200);
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getDetails(@PathVariable String id) {
        log.info("get Details - Controller - START");
        Product productDetails = productService.getProductDetails(id);
        log.info("get Details - Controller - END - {}", productDetails);
        return new ResponseEntity<>(productDetails, null, 200);
    }

    @DeleteMapping("cancel-order/{id}")
    public ResponseEntity<CancelOrderStatusResponse> cancelOrder(@PathVariable String id) {
        log.info("cancel order - Controller - START - id: {}", id);
        CancelOrderStatusResponse cancelOrderResponse = orderService.cancelOrder(id);
        log.info("cancel order - Controller - END");
        return new ResponseEntity<>(cancelOrderResponse, null, 200);
    }

//    Tests
    @GetMapping("/generate")
    public Order makeOrder() {
        Order order = new Order();
        ProductOrder productOrder1 = new ProductOrder();

        Product aproduct = new Product();
        aproduct.setId("1");
        aproduct.setName("name1");
        aproduct.setProductCode("001");
        aproduct.setQuantity("8");
        aproduct.setPrice("11.99");
        aproduct.setStatus("OK");
        aproduct.setType("TV");
        aproduct.setSubtype("FLAT_SCREEN");

        Product bproduct = new Product();
        bproduct.setId("2");
        bproduct.setName("name2");
        bproduct.setProductCode("002");
        bproduct.setQuantity("88");
        bproduct.setPrice("999");
        bproduct.setStatus("OK");
        bproduct.setType("TV");
        bproduct.setSubtype("FLAT_SCREEN");


        Product cproduct = new Product();
        cproduct.setId("3");
        cproduct.setName("name3");
        cproduct.setProductCode("003");
        cproduct.setQuantity("76");
        cproduct.setPrice("12.99");
        cproduct.setStatus("OK");
        cproduct.setType("TV");
        cproduct.setSubtype("FLAT_SCREEN");


        productOrder1.setProductId(aproduct.getId());
        productOrder1.setQuantityBought("5");

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setProductId(bproduct.getId());
        productOrder2.setQuantityBought("11");

        ProductOrder productOrder3 = new ProductOrder();
        productOrder3.setProductId(cproduct.getId());
        productOrder3.setQuantityBought("1");
        List<ProductOrder> orders = new ArrayList<>();
        orders.add(productOrder1);
        orders.add(productOrder2);
        orders.add(productOrder3);
        order.setProducts(orders);

        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("50-000");
        address.setCity("City");
        address.setCountry("Country");
        address.setPhoneNumber("55555555555");
        address.setEmail("asdasdassa");

        order.setAddress(address);
        order.setIsPayed(true);
        order.setDeliveryMethod(DeliveryMethodEnum.POST);
        order.setPaymentMethod(PaymentMethodEnum.CASH);

        return order;
    }

    @GetMapping("/test")
    public void tests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());
        Jwt jwt = (Jwt) authentication.getPrincipal();
        log.info(jwt.getSubject());
        log.info(jwt.getTokenValue());
        log.info(jwt.getClaims().toString());
        log.info((String) jwt.getClaims().get("preferred_username"));
        log.info(authentication.getDetails().toString());
        log.info(authentication.getAuthorities().toString());
    }

}
