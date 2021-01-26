package com.bgpark.letshadow.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;
    private int code;
    private String status;

    @Builder
    public ErrorResponse(String message, int code, String status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .build();
    }
}
