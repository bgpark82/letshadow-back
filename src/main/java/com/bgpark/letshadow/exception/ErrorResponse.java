package com.bgpark.letshadow.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;
    private String code;
    private int status;

    @Builder
    public ErrorResponse(String message, String code, int status) {
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
