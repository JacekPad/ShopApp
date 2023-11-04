package com.pad.app.service;

import com.pad.app.exception.badRequest.MessageTemplateException;
import com.pad.app.model.enums.MessageType;
import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.swagger.model.DeliveryMethodEnum;
import com.pad.app.swagger.model.Order;
import com.pad.app.swagger.model.PaymentMethodEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest(properties = { "spring.rabbitmq.template.exchange=exchangeName"})
class WorkerServiceTest {



    @Mock
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    WorkerService workerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(workerService, "sendOrderRoutingKey", "sendOrderRoutingKey");
        ReflectionTestUtils.setField(workerService, "productCountChangeRoutingKey", "productCountChangeRoutingKey");
        ReflectionTestUtils.setField(workerService, "exchangeName", "exchangeName");
    }

    private MessageTemplate createMessageTemplate() {
        OrderMessageTemplate template = new OrderMessageTemplate();
        template.setOrder(createOrder());
        return template;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setProducts(null);
        order.setAddress(null);
        order.setIsPayed(false);
        order.setStatus("INITIAL");
        order.setDeliveryMethod(DeliveryMethodEnum.COURIER);
        order.setPaymentMethod(PaymentMethodEnum.CASH);
        return order;
    }
    @Test
    void prepareMessage_shouldSendCorrectMessageType() {
        MessageTemplate template = createMessageTemplate();
//        when
        workerService.prepareMessage(template);
//        then
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(MessageTemplate.class));
    }

}