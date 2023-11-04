package com.pad.warehouse.exception;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pad.warehouse.service.LogService;
import com.pad.warehouse.swagger.model.ErrorResponse;
import com.pad.warehouse.swagger.model.ResponseHeader;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class RESTExceptionHandler {

    private final LogService logService;
    
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ErrorResponse> exceptionResponse(AbstractException exception) {
        logService.saveToErrorLog(exception);
        ErrorResponse response = createErrorResponse(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(response, null, exception.getCode());
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
