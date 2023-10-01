package com.pad.app.model.enums;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum MessageType implements Serializable {

    CHANGE_PRODUCT_COUNT,
    SEND_ORDER
}
