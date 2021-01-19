package com.bgpark.letshadow.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleApiException(ApiException e) {
        log.error("ApiException : {}", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
