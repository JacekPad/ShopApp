package com.pad.warehouse.exception.internal;

import org.springframework.http.HttpStatus;

import com.pad.warehouse.exception.AbstractException;

public class FetchDataError extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.INTERNAL_SERVER_ERROR;

    public FetchDataError(String message) {
        super(CODE, message);
    }
}
