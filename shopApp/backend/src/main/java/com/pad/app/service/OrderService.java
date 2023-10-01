package com.pad.app.service;

import java.util.List;

import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.pad.app.model.Order;
import com.pad.app.model.ProductOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ProductService productService;

    private final ManageOrderService manageOrderService;

    public void makeOrder(Order order) {
        log.info("makeOrder - Service - START: {}", order);
        List<ProductOrder> productOrderList = order.getProducts();

        if (isOrderAvailable(productOrderList)) {
            productOrderList.parallelStream().forEach(this::processProductOrder);
            processOrder(order);
        } else {
//            TODO some error that product are not available
        }
        log.info("makeOrder - Service - STOP");
    }

    private void processOrder(Order order) {
        log.info("processOrder - Service - START: {}", order);
        manageOrderService.sendOrder(order);
        log.info("processOrder - Service - STOP");
    }

    private void processProductOrder(ProductOrder productOrder) {
        log.info("ProcessProductOrder - Service - Start: {}", productOrder);
        int quantityBought = -productOrder.getQuantityBought();
        String productId = productOrder.getProduct().getId();
        productService.updateProductAvailability(productId, quantityBought);
        log.info("ProcessProductOrder - Service - Stop");
    }

    private boolean isOrderAvailable(List<ProductOrder> productOrders) {
        log.info("isOrderAvailable - Service - START: {}", productOrders);
        boolean isAvailable = productOrders.parallelStream()
                .allMatch(productService::isProductAvailable);
        log.info("isOrderAvailable - Service - STOP: isAvailable: {}", isAvailable);
        return isAvailable;
    }

}
