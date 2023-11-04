package com.pad.orderapp.exception.badRequest;

import com.pad.orderapp.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CancelOrderException extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.BAD_REQUEST;

    public CancelOrderException(String message) {
        super(CODE, message);
    }

}
