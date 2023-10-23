package com.pad.orderapp.exception.internal;

import com.pad.orderapp.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class FetchDataError extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.INTERNAL_SERVER_ERROR;

    public FetchDataError(String message) {
        super(CODE, message);
    }
}
