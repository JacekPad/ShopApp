package com.pad.app.model.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

    private List<ProductOrderDTO> products;
    private String address;
    private boolean isPayed;
    private String deliveryMethod;
    private String paymentMethod;
    private String orderPrice;
}
