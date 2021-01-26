package com.bgpark.letshadow.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("ApiException : {}", e.getErrorCode());

        final ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        final HttpStatus status = HttpStatus.valueOf(e.getErrorCode().getStatus());

        return new ResponseEntity(response, status);
    }
}
