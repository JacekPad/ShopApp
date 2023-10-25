package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.model.entity.AddressEntity;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.entity.ProductOrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.AddressRepository;
import com.pad.orderapp.repository.OrderRepository;
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
class AddressOrderServiceTest {


    @Mock
    AddressRepository addressRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    AddressOrderService addressOrderService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrderEntity createOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setProductOrdered(createProductEntityList());
        orderEntity.setAddress(createAddressEntity());
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
        return productOrder;
    }

    private AddressEntity createAddressEntity() {
        AddressEntity address = new AddressEntity();
        address.setStreet("Test Street 2");
        address.setZipCode("99-999");
        address.setCity("City");
        address.setCountry("PL");
        address.setPhoneNumber("111999111");
        address.setEmail("test@test.com");
        return address;
    }



    @Test
    void saveAddress_shouldSaveAddress() {
        AddressEntity address = createAddressEntity();
        address.setId(1L);
        OrderEntity orderEntity = createOrderEntity();

//        when
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

//        then
        try {
            addressOrderService.saveAddress(address, 1L);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void saveAddress_shouldThrowExceptionIfAddressDoesntExist() {
        AddressEntity address = createAddressEntity();
        OrderEntity orderEntity = createOrderEntity();

//        when
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

//        then
        try {
            addressOrderService.saveAddress(address, 1L);
            fail();
        } catch (Exception e) {
            assertInstanceOf(SaveObjectException.class, e);
            assertEquals("Could not save address - Address not processed properly", e.getMessage());
        }
    }

    @Test
    void saveAddress_shouldThrowExceptionIfOrderNotFound() {
        AddressEntity address = createAddressEntity();
        address.setId(1L);

//        when
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

//        then
        try {
            addressOrderService.saveAddress(address, 1L);
            fail();
        } catch (Exception e) {
            assertInstanceOf(NoObjectFound.class, e);
            assertEquals("No order present for id: 1", e.getMessage());
        }
    }

}