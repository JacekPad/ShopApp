package com.pad.orderapp.service;

import com.pad.orderapp.model.entity.AddressEntity;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.repository.AddressRepository;
import com.pad.orderapp.repository.OrderRepository;
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


    public void saveAddress(AddressEntity address, Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            address.setOrder(orderEntity.get());
            addressRepository.saveAndFlush(address);
        } else {
//            TODO some error
        }
    }
}
