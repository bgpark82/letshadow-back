package com.bgpark.letshadow.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST.value();
    }
}
