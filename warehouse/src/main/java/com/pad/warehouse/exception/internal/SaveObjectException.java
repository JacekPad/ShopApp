package com.pad.warehouse.exception.internal;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class SaveObjectException extends AbstractException {
    private static final HttpStatus CODE = HttpStatus.INTERNAL_SERVER_ERROR;

    public SaveObjectException(String message) {
        super(CODE, message);
    }
    
}
