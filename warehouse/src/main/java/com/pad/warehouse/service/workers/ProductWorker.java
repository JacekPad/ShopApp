package com.pad.warehouse.service.workers;

import com.pad.warehouse.model.DTOs.ProductQuantityChangeMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = {"${spring.rabbitmq.template.queue.productCountChange}"})
@Slf4j
public class ProductWorker {
//TODO not getting response?
// service or component?

    @RabbitHandler
    public void changeProductQuantityWorker(ProductQuantityChangeMessageTemplate messageTemplate) {
        log.info("got message: {}", messageTemplate);

//        TODO get rabbitmq message and push it to service
//         + log or something and test if done in multi threads?
    }
}
