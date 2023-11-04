package com.pad.app.service;

import com.pad.app.exception.notFound.NoObjectFound;
import com.pad.app.service.webClient.WebClientService;
import com.pad.app.swagger.model.DeliveryMethodEnum;
import com.pad.app.swagger.model.Order;
import com.pad.app.swagger.model.PaymentMethodEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageOrderServiceTest {

    @Mock
    WorkerService workerService;

    @Mock
    WebClientService webClientService;

    @InjectMocks
    ManageOrderService manageOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void sendOrder_whenOrderNull_shouldThrowException() {
        try {
            manageOrderService.sendOrder(null);
        } catch (Exception e) {
            assertInstanceOf(NoObjectFound.class, e);
            assertEquals("No order to send", e.getMessage());
        }
    }


    @Test
    void sendOrder_shouldSendOrder() {
        Order order = createOrder();
//        when
//        then
        manageOrderService.sendOrder(order);
        verify(workerService, times(1)).prepareMessage(any());
    }
}