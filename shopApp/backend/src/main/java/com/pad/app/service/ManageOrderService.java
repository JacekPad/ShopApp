package com.pad.app.service;

import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.swagger.model.Order;
import com.pad.app.swagger.model.OrderFilterParams;
import com.pad.app.swagger.model.OrdersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageOrderService {

    private final WorkerService workerService;

    private final WebClientService webClientService;

    @Value("${order.get-all.uri}")
    private String ORDER_URI;

    public void sendOrder(Order order) {
        OrderMessageTemplate orderMessageTemplate = prepareOrderTemplate(order);
        workerService.prepareMessage(orderMessageTemplate);
    }


    private OrderMessageTemplate prepareOrderTemplate(Order order) {
        OrderMessageTemplate messageTemplate = new OrderMessageTemplate();
        messageTemplate.setOrder(order);
        return messageTemplate;
    }


    public List<Order> getOrders(OrderFilterParams params) {
//        TODO cachable by ID? save order not the list?
        OrdersResponse orders = webClientService.webClientPost(ORDER_URI, OrdersResponse.class, params);
        log.info("TEMP orders: {}", orders);
        return orders.getOrders();
    }


}
