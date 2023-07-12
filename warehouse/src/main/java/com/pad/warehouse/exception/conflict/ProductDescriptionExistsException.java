package com.pad.warehouse.exception.conflict;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class ProductDescriptionExistsException extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.CONFLICT;

    public ProductDescriptionExistsException(String message) {
        super(CODE, message);
    }
    
}
