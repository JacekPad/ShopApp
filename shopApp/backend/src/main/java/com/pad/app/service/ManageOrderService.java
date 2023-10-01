package com.pad.app.service;

import com.pad.app.model.Order;
import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageOrderService {

    private final WorkerService workerService;

    public void sendOrder(Order order) {
        OrderMessageTemplate orderMessageTemplate = prepareOrderTemplate(order);
        workerService.prepareMessage(orderMessageTemplate);
    }


    private OrderMessageTemplate prepareOrderTemplate(Order order) {
        OrderMessageTemplate messageTemplate = new OrderMessageTemplate();
        messageTemplate.setOrder(order);
        return messageTemplate;
    }
}
