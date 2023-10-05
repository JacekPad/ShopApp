package com.pad.warehouse.service.workers;

import com.pad.warehouse.model.DTOs.ProductQuantityChangeMessageTemplate;
import com.pad.warehouse.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = {"${spring.rabbitmq.template.queue.productCountChange}"})
@Slf4j
@AllArgsConstructor
public class ProductWorker {
    private final ProductService productService;

    @RabbitHandler
    public void changeProductQuantityWorker(ProductQuantityChangeMessageTemplate messageTemplate) {
        log.info("WORKER - received message: {}", messageTemplate);
        productService.changeProductQuantity(messageTemplate.getProductId(), messageTemplate.getQuantity());
    }
}
