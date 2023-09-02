package com.pad.app.service;

import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessengerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.routing-key.sendOrder}")
    private String sendOrderRoutingKey;

    @Value("${spring.rabbitmq.template.routing-key.productCountChange}")
    private String productCountChangeRoutingKey;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    public <T> void prepareMessage(MessageTemplate messageTemplate) {
        log.info("preparing message: {} of type {}", messageTemplate, messageTemplate.getClass());
        log.info("type: {}", messageTemplate.getMessageType());
//        TODO instead of generics (listeners have to be not ambigous, so objects have to be unique) make abstract class and some subclasses and cast
//        messages to subclasses when receiving / sending
        switch (messageTemplate.getMessageType()) {
            case SEND_ORDER -> sendMessage(messageTemplate, sendOrderRoutingKey);
            case CHANGE_PRODUCT_COUNT -> sendMessage(messageTemplate, productCountChangeRoutingKey);
        }
    }

    private <T> void sendMessage(MessageTemplate message, String routingKey) {
        log.info("sending message: {}, of type: {}", message, message.getClass());
        log.info("thread: {}", Thread.currentThread().getName());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
