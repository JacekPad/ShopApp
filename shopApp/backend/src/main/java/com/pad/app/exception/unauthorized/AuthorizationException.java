package com.pad.app.exception.unauthorized;

import com.pad.app.exception.AbstractException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class AuthorizationException extends AbstractException {
    private static final HttpStatus CODE = HttpStatus.UNAUTHORIZED;

    public AuthorizationException(String message) {
        super(CODE, message);
    }
}
