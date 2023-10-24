package com.pad.app.exception.notFound;

import com.pad.app.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class NoObjectFound extends AbstractException {

    public static final HttpStatus CODE = HttpStatus.NOT_FOUND;

    public NoObjectFound(String message) {
        super(CODE, message);
    }
    
}
