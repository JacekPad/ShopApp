package com.pad.orderapp.service.worker;

import com.pad.orderapp.model.DTO.OrderMessageTemplate;
import com.pad.orderapp.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
@Slf4j
public class OrderWorker {

    private final OrderService orderService;

    @RabbitHandler
    public void changeProductQuantityWorker(OrderMessageTemplate messageTemplate) {
        log.info("WORKER - received message: {}", messageTemplate);
        orderService.processOrder(messageTemplate.getOrder());
    }

}
