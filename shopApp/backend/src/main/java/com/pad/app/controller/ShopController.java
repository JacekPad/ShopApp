package com.pad.app.controller;

import com.pad.app.model.FilterParams;
import com.pad.app.model.Order;
import com.pad.app.model.OrderAddress;
import com.pad.app.model.ProductOrder;
import com.pad.app.model.enums.DeliveryMethodEnum;
import com.pad.app.model.enums.PaymentMethodEnum;
import com.pad.app.service.OrderService;
import com.pad.app.service.ProductService;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
// make order and descrease number of products in warehouse and send order to other service
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductList>> getProducts(FilterParams params) {
//        TODO maybe less info when only querying for products?
        log.info("getProducts - Controller - START");
        log.error("TEMP LOG params: {}", params);
        List<ProductList> products = productService.getProducts(params);
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

//    TODO keep for making tests
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


        productOrder1.setProduct(aproduct);
        productOrder1.setQuantityBought(5);

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setProduct(bproduct);
        productOrder2.setQuantityBought(11);

        ProductOrder productOrder3 = new ProductOrder();
        productOrder3.setProduct(cproduct);
        productOrder3.setQuantityBought(1);
        List<ProductOrder> orders = new ArrayList<>();
        orders.add(productOrder1);
        orders.add(productOrder2);
        orders.add(productOrder3);
        order.setProducts(orders);

        OrderAddress address = new OrderAddress();
        address.setStreet("Street");
        address.setZipCode("50-000");
        address.setCity("City");
        address.setCountry("Country");
        address.setPhoneNumber("55555555555");
        address.setEmail("asdasdassa");

        order.setAddress(address);
        order.setPayed(true);
        order.setDeliveryMethod(DeliveryMethodEnum.POST);
        order.setPaymentMethod(PaymentMethodEnum.CASH);

        return order;
    }

}
