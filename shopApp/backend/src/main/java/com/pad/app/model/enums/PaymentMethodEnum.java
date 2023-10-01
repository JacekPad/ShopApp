package com.pad.app.model.enums;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum PaymentMethodEnum implements Serializable {

    CASH,
    CREDIT_CARD,
    BLIK
}
