package com.pad.app.service;

import java.util.List;

import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.model.messageTemplates.ProductQuantityChangeContent;
import org.springframework.stereotype.Service;

import com.pad.app.model.Order;
import com.pad.app.model.Product;
import com.pad.app.model.ProductOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;

    private final MessengerService messengerService;

    public void makeOrder(Order order) {
        List<ProductOrder> productOrderList = order.getProducts();

        if (isOrderAvailable(productOrderList)) {
            productOrderList.forEach(this::processProductOrder);
            processOrder(order);
        }

        // prepare order details and send to other service
        // descrease number of items in warehouse
        // move user somewhere else if 200OK etc.

    }

    private void decreaseItemQuantity(int byNumber, Product product) {

//        TODO product ID and test
        ProductQuantityChangeContent productQuantityChangeContent = new ProductQuantityChangeContent();
        productQuantityChangeContent.setQuantity(byNumber);
        productQuantityChangeContent.setProductId(2L);
        // TODO send rabbitQ to warehouse to decrease num of items
        MessageTemplate<ProductQuantityChangeContent> messageTemplate = new MessageTemplate<>();
        messageTemplate.setMessageContent(productQuantityChangeContent);
        messengerService.sendMessage(messageTemplate);
    }

    private void processOrder(Order order) {

        // TODO send order to Orders app
        MessageTemplate<Order> messageTemplate = new MessageTemplate<>();
        messageTemplate.setMessageContent(order);
        messengerService.sendMessage(messageTemplate);
    }

    private void processProductOrder(ProductOrder productOrder) {
        Product product = productOrder.getProduct();
        int quantityBought = productOrder.getQuantityBought();
        decreaseItemQuantity(quantityBought, product);
    }

    private boolean isOrderAvailable(List<ProductOrder> productOrders) {
        // TODO check if items are available to buy or were bought in between making order and
        //  (rabbitMQ multithread check all at the same time?)
        //  is anyMatch multithreaded to send rabbitmq or needs to be used differently?
        return productOrders.stream()
                .anyMatch(productOrder -> !productService.isProductAvailable(productOrder.getProduct().getId()));
    }

}
