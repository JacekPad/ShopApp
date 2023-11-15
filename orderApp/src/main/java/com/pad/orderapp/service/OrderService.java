package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.FetchDataError;
import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.mappers.OrderMapper;
import com.pad.orderapp.swagger.model.CancelOrderStatusResponse;
import com.pad.orderapp.swagger.model.ChangeOrderStatusResponse;
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
            order.setStatus(OrderStatus.INITIAL.getCode());
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
        log.info("updateOrderStatus - SERVICE - START, order: {} to status: {}", orderEntity.getId(), changeStatusTo);
        orderEntity.setStatus(changeStatusTo);
        try {
            orderRepository.saveAndFlush(orderEntity);
        } catch (Exception e) {
            log.error("Error updating order status: {}, status: {}", orderEntity.getId(), changeStatusTo.getName());
            throw new SaveObjectException("Could not update order status for order: " + orderEntity.getId());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Status changed to: ");
        switch (changeStatusTo) {
            case INITIAL -> sb.append(OrderStatus.INITIAL.getName());
            case IN_PROGRESS -> sb.append(OrderStatus.IN_PROGRESS.getName());
            case READY -> sb.append(OrderStatus.READY.getName());
            case IN_DELIVERY -> sb.append(OrderStatus.IN_DELIVERY.getName());
            case DELIVERED -> sb.append(OrderStatus.DELIVERED.getName());
            case CANCELED -> sb.append(OrderStatus.CANCELED.getName());
        }
        log.info("updateOrderStatus - SERVICE - END, order: {}", orderEntity.getId());
        return sb.toString();
    }

    public CancelOrderStatusResponse cancelOrder(Long orderId) {
        log.info("Cancel order - SERVICE - START, Order: {}", orderId);
        CancelOrderStatusResponse response = new CancelOrderStatusResponse();
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        String message = "";
        if (orderEntity.isEmpty()) {
            log.error("No order found for id: {}", orderId);
            throw new NoObjectFound("No object for id: " + orderId);
        }
        if (canOrderBeCanceled(orderEntity.get())) {
            log.info("Canceling order: {}", orderEntity.get().getId());
            message = updateOrderStatus(orderEntity.get(), OrderStatus.CANCELED);
            response.setChanged(true);
        } else {
            log.info("Error canceling order: {}, status: {}", orderId, orderEntity.get().getStatus().getName());
            message = "Could not cancel order: " + orderId + ", status: " + orderEntity.get().getStatus().getName();
            response.setChanged(false);
        }
        try {
            response.setOrder(mapper.mapToDataOrder(orderEntity.get()));
        } catch (Exception e) {
            log.error("Error mapping order entity to data: {}", orderEntity);
            throw new FetchDataError("Error while canceling order: " + orderId);
        }
        response.setMessage(message);
        return response;
    }

    private boolean canOrderBeCanceled(OrderEntity orderEntity) {
        return OrderStatus.INITIAL.equals(orderEntity.getStatus()) ||
                OrderStatus.IN_PROGRESS.equals(orderEntity.getStatus()) ||
                OrderStatus.READY.equals(orderEntity.getStatus());
    }

    public List<Order> getOrdersByParams(OrderFilterParams params) {
        log.info("getOrderByParams - SERVICE - START: {}", params);
        List<Order> orders = new ArrayList<>();
        List<OrderEntity> orderEntity = getOrders(params);
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

    public List<OrderEntity> getOrders(OrderFilterParams params) {
        try {
            return orderRepository.findByQueryParams(params.getCreatedBefore(), params.getCreatedAfter(), params.getStatus(), params.isIsPayed(), params.getUser());
        } catch (Exception e) {
            log.error("Error fetching orders from database: filter params: {}", params);
            throw new FetchDataError("Could not fetch order");
        }
    }
}
