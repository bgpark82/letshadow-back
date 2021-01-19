package com.bgpark.letshadow.domain.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String access_token;
    private String refresh_token;

    @Builder
    public GoogleToken(String email, String access_token, String refresh_token) {
        this.email = email;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}
