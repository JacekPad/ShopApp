package com.pad.app.service;

import java.util.List;


import com.pad.app.exception.notFound.NoObjectFound;
import com.pad.app.swagger.model.CancelOrderStatusResponse;
import com.pad.app.swagger.model.Order;
import com.pad.app.swagger.model.OrderFilterParams;
import com.pad.app.swagger.model.ProductOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



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
            log.info("makeOrder - Service - STOP");
        } else {
            log.error("Error making order - product is not available: {}", productOrderList);
            throw new NoObjectFound("Product in product list unavailable");
        }
    }

    public List<Order> getOrders(OrderFilterParams params) {
        log.info("get orders - service - START - params: {}", params);
        List<Order> orders = manageOrderService.getOrders(params);
        log.info("get orders - service - END");
        return orders;
    }

    private void processOrder(Order order) {
        log.info("processOrder - Service - START: {}", order);
        manageOrderService.sendOrder(order);
        log.info("processOrder - Service - STOP");
    }

    private void processProductOrder(ProductOrder productOrder) {
        log.info("ProcessProductOrder - Service - Start: {}", productOrder);
        int quantityBought = -Integer.parseInt(productOrder.getQuantityBought());
        String productId = productOrder.getProductId();
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

    public CancelOrderStatusResponse cancelOrder(String id) {
        CancelOrderStatusResponse cancelOrderResponse = manageOrderService.cancelOrder(id);
        if (cancelOrderResponse.isChanged()) {
            List<ProductOrder> products = cancelOrderResponse.getOrder().getProducts();
            products.forEach(productOrder -> {
                int quantityBought = Integer.parseInt(productOrder.getQuantityBought());
                productService.updateProductAvailability(productOrder.getProductId(), quantityBought);
            });
        }
        return cancelOrderResponse;
    }
}
