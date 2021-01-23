package com.bgpark.letshadow.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiException> handleApiException(ApiException e, HttpServletRequest request) {
        log.error("ApiException : {}", e);
        return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
    }
}
