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

    private final WorkerService workerService;

    private final ManageOrderService manageOrderService;

    public void makeOrder(Order order) {
        log.info("makeOrder - Service - START: {}", order);
        List<ProductOrder> productOrderList = order.getProducts();

        if (isOrderAvailable(productOrderList)) {
            productOrderList.parallelStream().forEach(this::processProductOrder);
            processOrder(order);
        }
        log.info("makeOrder - Service - STOP");
    }

    private void processOrder(Order order) {
        manageOrderService.sendOrder(order);
        // TODO send order to Orders app
        OrderMessageTemplate messageTemplate = new OrderMessageTemplate();
        messageTemplate.setOrder(order);
        workerService.prepareMessage(messageTemplate);
    }

    private void processProductOrder(ProductOrder productOrder) {
        log.info("ProcessProductOrder - Service - Start: {}", productOrder);
        int quantityBought = -productOrder.getQuantityBought();
        String productId = productOrder.getProduct().getId();
        productService.updateProductAvailability(productId, quantityBought);
        log.info("ProcessProductOrder - Service - Stop");
    }

    private boolean isOrderAvailable(List<ProductOrder> productOrders) {
        log.info("isOrderAvailable: START");
        // TODO check if items are available to buy or were bought in between making order and
        //  (rabbitMQ multithread check all at the same time?)
        //  is anyMatch multithreaded to send rabbitmq or needs to be used differently?
        boolean isAvailable = productOrders.parallelStream()
                .allMatch(productService::isProductAvailable);
        log.info("isOrderAvailable: STOP");
        log.error("is available?: {}", isAvailable);
        return isAvailable;
    }

}
