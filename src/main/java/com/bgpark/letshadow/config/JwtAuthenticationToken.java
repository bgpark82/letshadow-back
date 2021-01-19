package com.bgpark.letshadow.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private final String credentials;

    private final Object details;

    public JwtAuthenticationToken(Object principal, String credentials, Object details) {
        super(null);
        this.details = details;
        setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }



    public JwtAuthenticationToken(Object principal, String credentials, Object details, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.details = details;
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
