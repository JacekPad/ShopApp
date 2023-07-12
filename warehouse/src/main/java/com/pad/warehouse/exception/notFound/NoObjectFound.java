package com.pad.warehouse.exception.notFound;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class NoObjectFound extends AbstractException{

    public static final HttpStatus CODE = HttpStatus.NOT_FOUND;

    public NoObjectFound(String message) {
        super(CODE, message);
    }
    
}
