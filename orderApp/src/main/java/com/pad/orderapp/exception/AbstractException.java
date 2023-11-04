package com.pad.orderapp.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {

    private final int code;

    private final Map<String, String> params;

    public AbstractException(HttpStatus code, String message, Map<String, String> map) {
        super(message);
        this.code = code.value();
        this.params = map;
    }

    public AbstractException(HttpStatus code, String message) {
        this(code, message, null);
    }

    public AbstractException(HttpStatus code) {
        this(code, "");
    }

    public AbstractException(HttpStatus code, Map<String, String> map) {
        this(code, "", map);
    }

}
