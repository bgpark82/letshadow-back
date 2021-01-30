package com.bgpark.letshadow.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    GOOGLE_TOKEN_EXPIRED("Google token has been expired", "5001", 403),
    GOOGLE_TOKEN_NOT_FOUND("Google token not found", "4004", 404),
    GOOGLE_TOKEN_INFO_INVALID_REQUEST("Google token info request is invalid", "4001", 403)
    ;

    private String message;
    private String code;
    private int status;

    ErrorCode(String message, String code, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
