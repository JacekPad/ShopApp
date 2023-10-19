package com.pad.orderapp.service;

import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.mappers.OrderMapper;
import com.pad.warehouse.swagger.model.Order;
import com.pad.warehouse.swagger.model.OrderFilterParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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

    public String updateOrderStatus(OrderEntity orderEntity, OrderStatus changeStatusTo) {
//        TODO some normal delete or whatever webclient and wait for response it deleted or some error (cannot be deleted if status changed between frontend info and request)
//            TODO some error if not found

        orderEntity.setStatus(changeStatusTo.name());
        orderRepository.saveAndFlush(orderEntity);
        StringBuilder sb = new StringBuilder();
        sb.append("Status changed to: ");
        switch (changeStatusTo) {
            case INITIAL -> sb.append(OrderStatus.INITIAL.name());
            case IN_PROGRESS -> sb.append(OrderStatus.IN_PROGRESS.name());
            case READY -> sb.append(OrderStatus.READY.name());
            case IN_DELIVERY -> sb.append(OrderStatus.IN_DELIVERY.name());
            case DELIVERED -> sb.append(OrderStatus.DELIVERED.name());
            case CANCELED -> sb.append(OrderStatus.CANCELED.name());
        }
        return sb.toString();
    }

    public String cancelOrder(Long orderId) {
//        TODO
//        if order canceled before some status -> cancel order and set product quantities back
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        if (canOrderBeCanceled(orderEntity)) {
            return updateOrderStatus(orderEntity, OrderStatus.CANCELED);
        } else {
//            TODO cannot be canceled throw some shit
            return null;
        }
    }

    private boolean canOrderBeCanceled(OrderEntity orderEntity) {
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

    public List<Order> getOrdersByParams(OrderFilterParams params) {
        List<Order> orders = new ArrayList<>();
//        TODO get orders for admin/user based on params
        String someUserObject = "";
        List<OrderEntity> orderEntity = productOrderService.getOrders(params, someUserObject);
        orderEntity.forEach(order -> {
            orders.add(mapper.mapToDataOrder(order));
        });
        return orders;
    }


}
