package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.FetchDataError;
import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.entity.ProductOrderEntity;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.repository.ProductOrderRepository;
import com.pad.orderapp.swagger.model.OrderFilterParams;
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
        log.info("Save product order - SERVICE - START, order: {}, products: {}", orderId, productOrders);
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            productOrders.forEach(productOrder -> {
                productOrder.setOrder(orderEntity.get());
                Optional<ProductOrderEntity> productOrderCheck = productOrderRepository.findById(productOrder.getId());
                if (productOrderCheck.isEmpty()) {
                    log.error("Product order was not processed properly: order id: {}, product Id: {}",orderId,  productOrder.getId());
                    throw new SaveObjectException("Could not save product order - Product not processed properly");
                }
                try {
                    productOrderRepository.saveAndFlush(productOrder);
                    log.info("Save product order - SERVICE - END, order: {}", orderId);
                } catch (Exception e) {
                    log.error("Error saving order product, product id: {}, error: {}", productOrder.getProductId(), e);
                    throw new SaveObjectException("Could not save order product for order: " + orderId);
                }
            });
        } else {
            log.error("Error saving order products - no order found with id: {}", orderId);
            throw new NoObjectFound("No order present for id: " + orderId);
        }
    }

}
