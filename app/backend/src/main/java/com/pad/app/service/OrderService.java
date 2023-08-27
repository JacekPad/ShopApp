package com.pad.app.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    public void makeOrder() {
// prepare order details and send to other service
// descrease number of items in warehouse
// move user somwhere else if 200OK etc.

    }

    private void decreaseItemQuantity() {
        // send rabbitQ to warehouse to decrease num of items
    }

    private void sendOrder() {
        // send order to Orders app
    }

    private void isAvailable() {
        // check if items are available to buy or were bought inbetween making order and sending it?
    }




}
