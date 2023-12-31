package com.pad.orderapp.controller;

import com.pad.orderapp.service.OrderService;
import com.pad.orderapp.swagger.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CancelOrderStatusResponse> cancelOrder(@PathVariable Long id) {
        log.info("cancelOrder - CONTROLLER - START: {}", id);
        CancelOrderStatusResponse response = orderService.cancelOrder(id);
        response.setResponseHeader(generateHeaders());
        log.info("cancelOrder - CONTROLLER - END");
        return new ResponseEntity<>(response, null, 200);
    }


    @GetMapping("orders")
    public ResponseEntity<ProcessOrderResponse> getOrders(OrderFilterParams params) {
        log.info("getOrders - CONTROLLER - START: params {}", params);
        ProcessOrderResponse response = new ProcessOrderResponse();
        List<Order> orders = orderService.getOrdersByParams(params);
        response.setOrders(orders);
        response.setResponseHeader(generateHeaders());
        log.info("getOrders - CONTROLLER - END");
        return new ResponseEntity<>(response, null, 200);
    }

    private ResponseHeader generateHeaders() {
        ResponseHeader header = new ResponseHeader();
        header.setRequestId(UUID.randomUUID());
        header.setTimestamp(OffsetDateTime.now());
        return header;
    }

}
