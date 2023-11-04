package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.entity.ProductOrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.repository.ProductOrderRepository;
import com.pad.orderapp.swagger.model.DeliveryMethodEnum;
import com.pad.orderapp.swagger.model.PaymentMethodEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductOrderServiceTest {


    @Mock
    ProductOrderRepository productOrderRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    ProductOrderService productOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrderEntity createOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setProductOrdered(createProductEntityList());
        orderEntity.setAddress(null);
        orderEntity.setPayed(false);
        orderEntity.setStatus(OrderStatus.INITIAL);
        orderEntity.setDeliveryMethod(DeliveryMethodEnum.COURIER);
        orderEntity.setPaymentMethod(PaymentMethodEnum.CASH);
        return orderEntity;
    }

    private List<ProductOrderEntity> createProductEntityList() {
        List<ProductOrderEntity> productOrders = new ArrayList<>();
        productOrders.add(createProductEntity(1L, 3));
        productOrders.add(createProductEntity(2L, 5));
        productOrders.add(createProductEntity(3L, 7));
        return productOrders;
    }

    private ProductOrderEntity createProductEntity(Long productId, int quantityBought) {
        ProductOrderEntity productOrder = new ProductOrderEntity();
        productOrder.setProductId(productId);
        productOrder.setQuantityBought(quantityBought);
        productOrder.setId(productId);
        return productOrder;
    }
    @Test
    void saveProductOrder_shouldSaveProductOrder() {
        List<ProductOrderEntity> list = createProductEntityList();
        OrderEntity orderEntity = createOrderEntity();

//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(productOrderRepository.findById(anyLong())).thenReturn(Optional.of(list.get(0)));
//        then
        try {
            productOrderService.saveProductOrder(list, 1L);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void saveProductOrder_shouldThrowExceptionIfProductOrderDoesntExist() {
        List<ProductOrderEntity> list = createProductEntityList();
        OrderEntity orderEntity = createOrderEntity();

//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(productOrderRepository.findById(anyLong())).thenReturn(Optional.empty());
//        then
        try {
            productOrderService.saveProductOrder(list, 1L);
            fail();
        } catch (Exception e) {
            assertInstanceOf(SaveObjectException.class, e);
            assertEquals("Could not save product order - Product not processed properly", e.getMessage());
        }
    }

    @Test
    void saveProductOrder_shouldThrowExceptionIfOrderNotFound() {
        List<ProductOrderEntity> list = createProductEntityList();

//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productOrderRepository.findById(anyLong())).thenReturn(Optional.of(list.get(0)));
//        then
        try {
            productOrderService.saveProductOrder(list, 1L);
            fail();
        } catch (Exception e) {
            assertInstanceOf(NoObjectFound.class, e);
            assertEquals("No order present for id: 1", e.getMessage());
        }
    }





}