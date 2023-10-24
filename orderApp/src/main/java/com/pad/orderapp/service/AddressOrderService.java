package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.AddressEntity;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.repository.AddressRepository;
import com.pad.orderapp.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AddressOrderService {

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public void saveAddress(AddressEntity address, Long orderId) {
        log.info("save address - SERVICE - START, order id: {}, address: {}", orderId, address);
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            address.setOrder(orderEntity.get());
            if (address.getId() != null) {
                log.error("Address already exists: order id: {}, address id: {}", orderId, address.getId());
                throw new SaveObjectException("Could not save address - address already exists");
            }
            try {
                addressRepository.saveAndFlush(address);
                log.info("save address - SERVICE - END, order id: {}", orderId);
            } catch (Exception e) {
                log.error("Error saving order address: {}", e);
                throw new SaveObjectException("Could not save order address for order" + orderId);
            }
        } else {
            log.error("Error saving order address - no order found with id: {}", orderId);
            throw new NoObjectFound("No order present for id: " + orderId);
        }
    }
}
