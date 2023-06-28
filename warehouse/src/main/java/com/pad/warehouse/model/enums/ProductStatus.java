package com.pad.warehouse.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    
    AVAILABLE("AVAILABLE","Available"),
    UNAVAILABLE("UNAVAILABLE","Unavailable"),
    LOW_QUANTITY("LOW_QUANTITY","low quantity"),
    DISABLED("DISABLED","Disabled");

    private final String code;
    private final String value;

}
