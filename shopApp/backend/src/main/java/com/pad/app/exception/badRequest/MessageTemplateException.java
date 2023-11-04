package com.pad.app.exception.badRequest;

import com.pad.app.exception.AbstractException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class MessageTemplateException extends AbstractException {

    private static final HttpStatus CODE = HttpStatus.BAD_REQUEST;


    public MessageTemplateException(String message) {
        super(CODE, message);
    }
}
