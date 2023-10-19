package com.pad.orderapp.controller;

import com.pad.orderapp.model.entity.OrderEntity;
import com.pad.orderapp.service.OrderService;
import com.pad.warehouse.swagger.model.Order;
import com.pad.warehouse.swagger.model.OrderFilterParams;
import com.pad.warehouse.swagger.model.ProcessOrderResponse;
import com.pad.warehouse.swagger.model.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @DeleteMapping("cancel-order/{id}")
    public ResponseEntity<ProcessOrderResponse> cancelOrder(@PathVariable Long id) {
//      TODO some response object
        log.info("cancelOrder - CONTROLLER - START: {}", id);
        String message = orderService.cancelOrder(id);
        ProcessOrderResponse response = new ProcessOrderResponse();
        ResponseHeader header = new ResponseHeader();
        header.setRequestId(UUID.randomUUID());
        header.setTimestamp(OffsetDateTime.now());
        response.setResponseHeader(header);
        response.setCode(String.valueOf(200));
        response.setMessage(message);
        log.info("cancelOrder - CONTROLLER - END");
        return new ResponseEntity<>(response, null, 200);
    }


    @RequestMapping("orders")
    public ResponseEntity<List<Order>> getOrders(OrderFilterParams params) {
//        TODO some identification?
        log.info("getOrders - CONTROLLER - START: ");
        List<Order> orders = orderService.getOrdersByParams(params);
        log.info("getOrders - CONTROLLER - END");
        return new ResponseEntity<>(orders, null, 200);
    }

}
