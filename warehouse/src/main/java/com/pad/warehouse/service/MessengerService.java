package com.pad.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessengerService {

    private final ProductService productService;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void changeProductQuantityListener(String test) {
        log.info("receiving test message from rabbitmq queue");
        log.info("Message: {}", test);

//     TODO   add product change content as a message received
        productService.changeProductQuantity(null);

    }
}
