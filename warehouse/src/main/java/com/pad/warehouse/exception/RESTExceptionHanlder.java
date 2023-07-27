package com.pad.warehouse.exception;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pad.warehouse.swagger.model.ErrorResponse;
import com.pad.warehouse.swagger.model.ResponseHeader;

@ControllerAdvice
public class RESTExceptionHanlder {
    
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ErrorResponse> exceptionResponse(AbstractException exception) {
        // TODO save exceptions to log db?
        ErrorResponse response = createErrorResponse(exception.getCode(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(response, null, exception.getCode());
    }

    private ErrorResponse createErrorResponse (int code, String message) {
        ResponseHeader header = new ResponseHeader();
        header.setRequestId(UUID.randomUUID());
        header.setTimestamp(OffsetDateTime.now());
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        response.setCode(String.valueOf(code));
        response.setResponseHeader(header);
        return response;
    }
}
