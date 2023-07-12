package com.pad.warehouse.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pad.warehouse.swagger.model.ErrorResponse;
import com.pad.warehouse.swagger.model.ResponseHeader;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RESTExceptionHanlder {
    

    // @ExceptionHandler(CustomError.class)
    // public ResponseEntity<ErrorResponse> customError(CustomError error) {
    //     ErrorResponse response = new ErrorResponse();
    //     response.setMessage(error.getMessage());
    //     response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
    //     return new ResponseEntity<ErrorResponse>(response, null, HttpStatus.BAD_REQUEST.value());
    // }
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ErrorResponse> exceptionResponse(AbstractException exception) {
        log.info("excpetion: {}", exception);
        log.info("excp code: {}", exception.getCode());
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
