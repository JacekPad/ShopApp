package com.pad.warehouse.exception.conflict;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class ProductExistsException extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.CONFLICT;

    public ProductExistsException(String message) {
        super(CODE, message);
    }

}
