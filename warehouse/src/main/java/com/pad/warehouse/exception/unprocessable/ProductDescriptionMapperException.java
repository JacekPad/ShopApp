package com.pad.warehouse.exception.unprocessable;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class ProductDescriptionMapperException extends AbstractException{
    
    private static final HttpStatus CODE = HttpStatus.UNPROCESSABLE_ENTITY;

    public ProductDescriptionMapperException(String message) {
        super(CODE, message);
    }
    
}
