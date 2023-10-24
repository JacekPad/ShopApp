package com.pad.orderapp.service;

import com.pad.orderapp.exception.badRequest.CancelOrderException;
import com.pad.orderapp.exception.internal.FetchDataError;
import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.mappers.OrderMapper;
import com.pad.orderapp.swagger.model.Order;
import com.pad.orderapp.swagger.model.OrderFilterParams;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressOrderService addressOrderService;
    private final ProductOrderService productOrderService;
    private final OrderMapper mapper;

    @Transactional
    public void processOrder(Order order) {
        log.info("processOrder - SERVICE - START: {}", order);
        try {
            OrderEntity orderEntity = orderRepository.saveAndFlush(mapper.mapToEntityOrder(order));
            addressOrderService.saveAddress(orderEntity.getAddress(), orderEntity.getId());
            productOrderService.saveProductOrder(orderEntity.getProductOrdered(), orderEntity.getId());
            log.info("processOrder - SERVICE - END");
        } catch (Exception e) {
            log.error("Error saving order details: {}", e.getStackTrace());
            throw new SaveObjectException("Could not save order data for order: " + order.getId());
        }
    }

    public String updateOrderStatus(OrderEntity orderEntity, OrderStatus changeStatusTo) {
//        TODO some normal delete or whatever webclient and wait for response it deleted or some error (cannot be deleted if status changed between frontend info and request)
//            TODO some error if not found
        log.info("updateOrderStatus - SERVICE - START, order: {} to status: {}", orderEntity.getId(), changeStatusTo);
        orderEntity.setStatus(changeStatusTo.name());
        try {
            orderRepository.saveAndFlush(orderEntity);
        } catch (Exception e) {
            log.error("Error updating order status: {}, status: {}", orderEntity.getId(), changeStatusTo.name());
            throw new SaveObjectException("Could not update order status for order: " + orderEntity.getId());
        }
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
        log.info("updateOrderStatus - SERVICE - END, order: {}", orderEntity.getId());
        return sb.toString();
    }

    public String cancelOrder(Long orderId) {
//       TODO if order canceled before some status -> cancel order and set product quantities back (in shop app quantity?)
        log.info("Cancel order - SERVICE - START, Order: {}", orderId);
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            log.error("No order found for id: {}", orderId);
            throw new NoObjectFound("No object for id: " + orderId);
        }
        if (canOrderBeCanceled(orderEntity.get())) {
            log.info("Canceling order: {}", orderEntity.get().getId());
            return updateOrderStatus(orderEntity.get(), OrderStatus.CANCELED);
        } else {
            log.error("Error canceling order: {}, status: {}", orderId, orderEntity.get().getStatus());
            throw new CancelOrderException("Could not cancel order: " + orderId);
        }
    }

    private boolean canOrderBeCanceled(OrderEntity orderEntity) {
        return OrderStatus.INITIAL.name().equals(orderEntity.getStatus()) ||
                OrderStatus.IN_PROGRESS.name().equals(orderEntity.getStatus()) ||
                OrderStatus.READY.name().equals(orderEntity.getStatus());
    }

    public List<Order> getOrdersByParams(OrderFilterParams params) {
        log.info("getOrderByParams - SERVICE - START: {}", params);
        List<Order> orders = new ArrayList<>();
//        TODO get orders for admin/user based on params
        String someUserObject = "";
        List<OrderEntity> orderEntity = getOrders(params, someUserObject);
        orderEntity.forEach(order -> {
            try {
                orders.add(mapper.mapToDataOrder(order));
            } catch (Exception e) {
                log.error("Error mapping order entity to data: {}", order.getId());
                throw new FetchDataError("Could not fetch order: " + order.getId());
            }
        });
        log.info("getOrderByParams - SERVICE - END");
        return orders;

    }

    public List<OrderEntity> getOrders(OrderFilterParams params, String someUserObject) {
        String user = "";
        try {
            return orderRepository.findByQueryParams(params.getCreatedBefore(), params.getCreatedAfter(), params.getStatus(), params.isIsPayed());
        } catch (Exception e) {
            log.error("Error fetching orders from database: filter params: {}", params);
            throw new FetchDataError("Could not fetch order");
        }
    }
}
