package com.pad.app.service;

import java.util.List;

import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import com.pad.warehouse.swagger.model.Product;
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

    public void makeOrder(Order order) {
        log.info("makeOrder START: {}", order);
        List<ProductOrder> productOrderList = order.getProducts();

        if (isOrderAvailable(productOrderList)) {
            productOrderList.parallelStream().forEach(this::processProductOrder);
            processOrder(order);
        }

        // prepare order details and send to other service
        // descrease number of items in warehouse
        // move user somewhere else if 200OK etc.
    log.info("makeOrder STOP");
    }

    private void decreaseItemQuantity(int byNumber, Product product) {

//        TODO product ID and test
        ProductQuantityChangeMessageTemplate productQuantityChangeMessageTemplate = new ProductQuantityChangeMessageTemplate();
        productQuantityChangeMessageTemplate.setQuantity(byNumber);
        productQuantityChangeMessageTemplate.setProductId(Long.parseLong(product.getId()));
        // TODO send rabbitQ to warehouse to decrease num of items
        workerService.prepareMessage(productQuantityChangeMessageTemplate);
    }

    private void processOrder(Order order) {

        // TODO send order to Orders app
        OrderMessageTemplate messageTemplate = new OrderMessageTemplate();
        messageTemplate.setOrder(order);
        workerService.prepareMessage(messageTemplate);
    }

    private void processProductOrder(ProductOrder productOrder) {
        //        TODO TESTING
        String thread = Thread.currentThread().getName();
        log.info("Current thread: {}", thread);
        Product product = productOrder.getProduct();
        int quantityBought = productOrder.getQuantityBought();
        decreaseItemQuantity(quantityBought, product);
    }

    private boolean isOrderAvailable(List<ProductOrder> productOrders) {
        log.info("isOrderAvailable: START");
        // TODO check if items are available to buy or were bought in between making order and
        //  (rabbitMQ multithread check all at the same time?)
        //  is anyMatch multithreaded to send rabbitmq or needs to be used differently?
        boolean isAvailable = productOrders.parallelStream()
                .anyMatch(productOrder -> !productService.isProductAvailable(productOrder));
        log.info("isOrderAvailable: STOP");
        log.error("is available?: {}", isAvailable);
//        return isAvailable;
        return true;
    }

}
