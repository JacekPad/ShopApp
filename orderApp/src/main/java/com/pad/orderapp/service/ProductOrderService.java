package com.pad.orderapp.service;

import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.entity.ProductOrderEntity;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.repository.ProductOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;

    public void saveProductOrder(List<ProductOrderEntity> productOrders, Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            productOrders.forEach(productOrder -> {
                productOrder.setOrder(orderEntity.get());
                productOrderRepository.saveAndFlush(productOrder);
            });
        } else {
//            TODO some error
        }

    }
}
