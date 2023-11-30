package com.pad.app.service;

import com.pad.app.exception.notFound.NoObjectFound;
import com.pad.app.factories.messagetemplate.OrderMessageFactory;
import com.pad.app.factories.messagetemplate.TemplateFactory;
import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.service.webClient.WebClientMappers;
import com.pad.app.service.webClient.WebClientService;
import com.pad.app.swagger.model.CancelOrderStatusResponse;
import com.pad.app.swagger.model.Order;
import com.pad.app.swagger.model.OrderFilterParams;
import com.pad.app.swagger.model.ProcessOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageOrderService {

    private final WorkerService workerService;

    private final WebClientService webClientService;

    private final WebClientMappers webClientMappers;

    @Value("${order.get-all.uri}")
    private String ORDER_URI;

    public void sendOrder(Order order) {
        if (order == null) {
            log.error("No order to send");
            throw new NoObjectFound("No order to send");
        }
        MessageTemplate orderMessageTemplate = prepareOrderTemplate(order);
        workerService.prepareMessage(orderMessageTemplate);
    }

    private MessageTemplate prepareOrderTemplate(Order order) {
        return TemplateFactory.createTemplate(new OrderMessageFactory(order));
    }

    public List<Order> getOrders(OrderFilterParams params) {
        String uri = ORDER_URI + "orders";
        MultiValueMap<String, String> uriParams = webClientMappers.convertToUriParams(params);
        ProcessOrderResponse orders = webClientService.webClientGet(uri, ProcessOrderResponse.class, uriParams);
        log.debug("orders: {}", orders);
        return orders.getOrders();
    }

    public CancelOrderStatusResponse cancelOrder(String id) {
        String uri = ORDER_URI + "cancel-order/" + id;
        CancelOrderStatusResponse cancelOrderResponse= webClientService.webClientDelete(uri, CancelOrderStatusResponse.class);
        return cancelOrderResponse;
    }


}
