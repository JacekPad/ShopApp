package com.pad.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pad.app.model.Order;
import com.pad.app.model.Product;
import com.pad.app.model.ProductOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;

    public void makeOrder(Order order) {
        List<ProductOrder> productOrderList = order.getProducts();

        if (isOrderAvailable(productOrderList)) {
            productOrderList.forEach(productOrder -> {
                processProductOrder(productOrder);
            });
            processOrder(order);
        }

        // prepare order details and send to other service
        // descrease number of items in warehouse
        // move user somewhere else if 200OK etc.

    }

    private void decreaseItemQuantity(int byNumber, Product product) {
        // send rabbitQ to warehouse to decrease num of items
    }

    private void processOrder(Order order) {
        // send order to Orders app
    }

    private void processProductOrder(ProductOrder productOrder) {
        Product product = productOrder.getProduct();
        int quantityBought = productOrder.getQuantityBought();
        decreaseItemQuantity(quantityBought, product);
    }

    private boolean isOrderAvailable(List<ProductOrder> productOrders) {
        // check if items are available to buy or were bought inbetween making order and
        // sending it?
        boolean isAllAvailable = productOrders.stream()
                .anyMatch(productOrder -> !productService.isProductAvailable(productOrder.getProduct()));
        return isAllAvailable;
    }

}
