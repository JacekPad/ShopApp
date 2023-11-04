package com.pad.orderapp.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    INITIAL("INITIAL", "Initial"),
    IN_PROGRESS("IN_PROGRESS","In progress"),
    READY("READY", "Ready"),
    IN_DELIVERY("IN_DELIVERY", "In delivery"),
    DELIVERED("DELIVERED", "Delivered"),
    CANCELED("CANCELED", "Canceled");

    private final String code;
    private final String name;

    private OrderStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

