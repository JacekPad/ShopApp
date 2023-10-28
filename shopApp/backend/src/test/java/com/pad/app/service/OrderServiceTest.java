package com.pad.app.service;

import com.pad.app.exception.notFound.NoObjectFound;
import com.pad.app.swagger.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private ManageOrderService manageOrderService;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order createOrder() {
        Order order = new Order();
        order.setProducts(createProductList());
        order.setAddress(createAddress());
        order.setIsPayed(false);
        order.setStatus("INITIAL");
        order.setDeliveryMethod(DeliveryMethodEnum.COURIER);
        order.setPaymentMethod(PaymentMethodEnum.CASH);
        return order;
    }

    private List<com.pad.app.swagger.model.ProductOrder> createProductList() {
        List<ProductOrder> productOrders = new ArrayList<>();
        productOrders.add(createProduct("1", "3"));
        productOrders.add(createProduct("2", "5"));
        productOrders.add(createProduct("3", "7"));
        return productOrders;
    }

    private ProductOrder createProduct(String productId, String quantityBought) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(productId);
        productOrder.setQuantityBought(quantityBought);
        return productOrder;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("Test Street 2");
        address.setZipCode("99-999");
        address.setCity("City");
        address.setCountry("PL");
        address.setPhoneNumber("111999111");
        address.setEmail("test@test.com");
        return address;
    }


    @Test
    void makeOrder_whenProductNotAvailable_throwException() {
//        when
        Order order = createOrder();
        when(productService.isProductAvailable(any())).thenReturn(false);
//        then
        try {
            orderService.makeOrder(order);
            fail();
        } catch (Exception e) {
            assertInstanceOf(NoObjectFound.class, e);
            assertEquals("Product in product list unavailable", e.getMessage());
        }
    }

    @Test
    void makeOrder_shouldProcessOrder() {
//        when
        Order order = createOrder();
        when(productService.isProductAvailable(any())).thenReturn(true);
//        then
        try {
            orderService.makeOrder(order);
            verify(manageOrderService, times(1)).sendOrder(any());
        } catch (Exception e) {
            fail();
        }
    }


}