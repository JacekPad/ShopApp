package com.pad.warehouse.exception.unprocessable;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class ProductMapperException extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.UNPROCESSABLE_ENTITY;

    public ProductMapperException(String message) {
        super(CODE, message);
    }
    
}
