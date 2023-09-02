package com.pad.app.model;

import java.io.Serializable;
import java.util.List;

import com.pad.app.model.enums.DeliveryMethodEnum;
import com.pad.app.model.enums.PaymentMethodEnum;

import lombok.Data;

@Data
public class Order implements Serializable {
    
    private List<ProductOrder> products;
    private OrderAddress address;
    private boolean isPayed;
    private DeliveryMethodEnum deliveryMethod;
    private PaymentMethodEnum paymentMethod;
}
