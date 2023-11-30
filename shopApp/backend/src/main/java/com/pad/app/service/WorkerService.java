package com.pad.app.service;

import com.pad.app.exception.badRequest.MessageTemplateException;
import com.pad.app.model.messageTemplates.MessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.routing-key.sendOrder}")
    private String sendOrderRoutingKey;

    @Value("${spring.rabbitmq.template.routing-key.productCountChange}")
    private String productCountChangeRoutingKey;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    public void prepareMessage(MessageTemplate messageTemplate) {
        log.info("preparing message: {} of type {}", messageTemplate, messageTemplate.getMessageType());
        log.debug("thread: {}", Thread.currentThread().getName());
        switch (messageTemplate.getMessageType()) {
            case SEND_ORDER -> sendMessage(messageTemplate, sendOrderRoutingKey);
            case CHANGE_PRODUCT_COUNT -> sendMessage(messageTemplate, productCountChangeRoutingKey);
            default -> throw new MessageTemplateException("Message type not supported");
        }
    }

    private void sendMessage(MessageTemplate message, String routingKey) {
        log.info("sending message: {}, of type: {}", message, message.getClass());
        log.debug("thread: {}", Thread.currentThread().getName());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
