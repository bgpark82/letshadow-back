package com.bgpark.letshadow.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    GOOGLE_TOKEN_EXPIRED("Google token has been expired", 500, "5001"),
    GOOGLE_TOKEN_NOT_FOUND("Google token not found", 404, "4004"),
    GOOGLE_TOKEN_INFO_INVALID_REQUEST("Google token info request is invalid", 400, "4001")
    ;

    private String message;
    private int code;
    private String status;

    ErrorCode(String message, int code, String status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
