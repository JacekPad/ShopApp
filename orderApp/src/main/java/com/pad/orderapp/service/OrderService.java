package com.pad.orderapp.service;

import com.pad.orderapp.model.entity.AddressEntity;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.AddressRepository;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.mappers.OrderMapper;
import com.pad.orderapp.repository.ProductOrderRepository;
import com.pad.warehouse.swagger.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressOrderService addressOrderService;
    private final ProductOrderService productOrderService;
    private final OrderMapper mapper;

    public void processOrder(Order order) {
        log.info("processOrder - SERVICE - START: {}", order);
        OrderEntity orderEntity = orderRepository.saveAndFlush(mapper.mapToEntityOrder(order));
        addressOrderService.saveAddress(orderEntity.getAddress(), orderEntity.getId());
        productOrderService.saveProductOrder(orderEntity.getProductOrdered(), orderEntity.getId());
        log.info("processOrder - SERVICE - END");
    }

    public boolean updateOrderStatus(Long orderId, OrderStatus changeStatusTo) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
//            TODO some error if not found
        if (OrderStatus.CANCELED.equals(changeStatusTo)) {
            if (OrderStatus.INITIAL.name().equals(orderEntity.getStatus()) ||
                    OrderStatus.IN_PROGRESS.name().equals(orderEntity.getStatus()) ||
                    OrderStatus.READY.name().equals(orderEntity.getStatus())) {
                orderEntity.setStatus(OrderStatus.CANCELED.name());
                orderRepository.saveAndFlush(orderEntity);
                return true;
//                TODO can be canceled
            } else {
//                TODO cannot be canceled anymore
                return false;
            }
        }

        switch (changeStatusTo) {
            case INITIAL -> {
                log.info("send some emails etc");
            }
            case IN_PROGRESS -> {
                log.info("send some emails etc");
            }
            case READY -> {
                log.info("send some emails etc");
            }
            case IN_DELIVERY -> {
                log.info("send some emails etc");
            }
            case DELIVERED -> {
                log.info("send some emails etc");
            }
        }
        orderEntity.setStatus(changeStatusTo.name());
        orderRepository.saveAndFlush(orderEntity);
        return true;

    }

    public void cancelOrder(Long orderId) {
//        TODO
//        if order canceled before some status -> cancel order and set product quantities back
        boolean isStatusChanged = updateOrderStatus(orderId, OrderStatus.CANCELED);
    }

    public void getOrdersByUser() {
//        TODO get orders for user based on request
    }

    public void getOrdersByParams() {
//        TODO get orders for admin based on params
    }


}
