package com.pad.app.exception.internal;

import com.pad.app.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class SaveObjectException extends AbstractException {
    private static final HttpStatus CODE = HttpStatus.INTERNAL_SERVER_ERROR;

    public SaveObjectException(String message) {
        super(CODE, message);
    }
    
}
