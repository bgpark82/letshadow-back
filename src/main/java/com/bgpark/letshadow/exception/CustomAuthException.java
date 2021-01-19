package com.bgpark.letshadow.exception;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

/**
 *
 * https://stackoverflow.com/questions/31660707/spring-oauth2-custom-exception-handler
 */
public class CustomAuthException extends ClientAuthenticationException {

    private String code;

    public CustomAuthException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomAuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public int getHttpErrorCode() {
        return super.getHttpErrorCode();
    }

    @Override
    public String getOAuth2ErrorCode() {
        return code;
    }
}
