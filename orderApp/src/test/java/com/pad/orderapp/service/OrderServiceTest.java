package com.pad.orderapp.service;

import com.pad.orderapp.exception.internal.FetchDataError;
import com.pad.orderapp.exception.internal.SaveObjectException;
import com.pad.orderapp.exception.notFound.NoObjectFound;
import com.pad.orderapp.mappers.OrderMapper;
import com.pad.orderapp.model.entity.AddressEntity;
import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.model.entity.ProductOrderEntity;
import com.pad.orderapp.model.enums.OrderStatus;
import com.pad.orderapp.repository.OrderRepository;
import com.pad.orderapp.swagger.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    AddressOrderService addressOrderService;

    @Mock
    ProductOrderService productOrderService;

    @Mock
    OrderMapper mapper;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order createOrder() {
        Order order = new Order();
        order.setProducts(createProductList());
        order.setAddress(createAddress());
        order.setIsPayed(false);
        order.setStatus("INITIAL");
        order.setDeliveryMethod(DeliveryMethodEnum.COURIER);
        order.setPaymentMethod(PaymentMethodEnum.CASH);
        return order;
    }

    private List<ProductOrder> createProductList() {
        List<ProductOrder> productOrders = new ArrayList<>();
        productOrders.add(createProduct("1", "3"));
        productOrders.add(createProduct("2", "5"));
        productOrders.add(createProduct("3", "7"));
        return productOrders;
    }

    private ProductOrder createProduct(String productId, String quantityBought) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(productId);
        productOrder.setQuantityBought(quantityBought);
        return productOrder;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("Test Street 2");
        address.setZipCode("99-999");
        address.setCity("City");
        address.setCountry("PL");
        address.setPhoneNumber("111999111");
        address.setEmail("test@test.com");
        return address;
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

    private OrderFilterParams createOrderFilterParams() {
        OrderFilterParams params = new OrderFilterParams();
        params.setCreatedBefore(OffsetDateTime.now().toString());
        params.setCreatedAfter(OffsetDateTime.now().toString());
        params.setStatus("INITIAL");
        params.setIsPayed(false);
        return params;
    }

    @Test
    void processOrder_shouldSaveProductOrder() {
        Order order = createOrder();
        OrderEntity orderEntity = createOrderEntity();
//        when

        when(mapper.mapToEntityOrder(any())).thenReturn(orderEntity);
        when(orderRepository.saveAndFlush(any())).thenReturn(orderEntity);
//        then
        orderService.processOrder(order);
    }

    @Test
    void processOrder_shouldThrowExceptionIfNotSaved() {
        Order order = createOrder();
        OrderEntity orderEntity = createOrderEntity();
//        when

        when(mapper.mapToEntityOrder(any())).thenThrow(new FetchDataError("test exception"));
        when(orderRepository.saveAndFlush(any())).thenReturn(orderEntity);
//        then
        try {
            orderService.processOrder(order);
            fail();
        } catch (Exception e) {
            assertInstanceOf(SaveObjectException.class, e);
        }
    }

    @Test
    void updateOrderStatus_shouldUpdateOrderStatus() {
        OrderEntity orderEntity = createOrderEntity();
        OrderStatus statusToChangeTo = OrderStatus.IN_DELIVERY;
        String expectedResponse = "Status changed to: " + OrderStatus.IN_DELIVERY.getName();
//        when

//        then
        String response = orderService.updateOrderStatus(orderEntity, statusToChangeTo);
        assertEquals(expectedResponse, response);
    }

    @Test
    void updateOrderStatus_shouldThrowExceptionIfSaveFailed() {
        OrderEntity orderEntity = createOrderEntity();
        OrderStatus statusToChangeTo = OrderStatus.IN_DELIVERY;
//        when
        when(orderRepository.saveAndFlush(any())).thenThrow(new FetchDataError(""));
//        then
        try {
            String response = orderService.updateOrderStatus(orderEntity, statusToChangeTo);
            fail();
        } catch (Exception e) {
            assertInstanceOf(SaveObjectException.class, e);
        }
    }

    @Test
    void cancelOrder_shouldCancelOrder() {
        OrderEntity orderEntity = createOrderEntity();
        String expectedResponse = "Status changed to: " + OrderStatus.CANCELED.getName();
//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

//        then
        CancelOrderStatusResponse response = orderService.cancelOrder(1L);
        assertEquals(expectedResponse, response.getMessage());
        assertTrue(response.isChanged());
    }

    @Test
    void cancelOrder_shouldNotCancelIfCannotBeCanceled() {
        OrderEntity orderEntity = createOrderEntity();
        orderEntity.setStatus(OrderStatus.IN_DELIVERY);
//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

//        then
        CancelOrderStatusResponse response = orderService.cancelOrder(1L);
        assertFalse(response.isChanged());
        assertEquals("Could not cancel order: 1, status: " + OrderStatus.IN_DELIVERY.getName(), response.getMessage());
    }

    @Test
    void cancelOrder_shouldThrowExceptionIfOrderNotFound() {
        OrderEntity orderEntity = createOrderEntity();
        orderEntity.setStatus(OrderStatus.IN_DELIVERY);
//        when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

//        then
        try {
            CancelOrderStatusResponse response = orderService.cancelOrder(1L);
            fail(response.getMessage());
        } catch (Exception e) {
            assertInstanceOf(NoObjectFound.class, e);
        }
    }

    @Test
    void getOrdersByParams_shouldThrowExceptionIfMappingFails() {
        OrderFilterParams params = createOrderFilterParams();

//        when
        when(orderRepository.findByQueryParams(any(), any(), any(), anyBoolean(), any()))
                .thenThrow(new FetchDataError(""));

//        then
        try {
            orderService.getOrdersByParams(params);
            fail();
        } catch (Exception e) {
            assertInstanceOf(FetchDataError.class, e);
        }
    }

}