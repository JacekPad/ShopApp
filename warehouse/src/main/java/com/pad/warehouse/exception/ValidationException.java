package com.pad.warehouse.exception;

import java.util.Map;
import java.util.StringJoiner;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class
 * Takes all validation errors and puts it in message
 * throw new ValidationException(*map<String, String>*)
 */
public class ValidationException extends AbstractException {

    private static final long serialVersionUID = -5689721647285L;

    private final Map<String, String> exceptions;

    public ValidationException(Map<String, String> exceptions, HttpStatus code) {
        super(code);
        this.exceptions = exceptions;
    }


    @Override
    public String getMessage() {
        return "Validation failed: " + createExceptionMessage();
    }

    private String createExceptionMessage() {
        StringJoiner st = new StringJoiner(", ", "[", "]");
        for (String key : exceptions.keySet()) {
            st.add("{" + key + " - " + exceptions.get(key) + "}");
        }
        return st.toString();
    }

}
