package com.bgpark.letshadow.domain.token;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoogleTokenRepositoryTest {

    @Autowired
    private GoogleTokenRepository googleTokenRepository;

    @Test
    void isTokenExpired() {
        GoogleToken token = GoogleToken.builder()
                .expires_in(3600)
                .build();
        GoogleToken savedToken = googleTokenRepository.save(token);
        assertThat(savedToken.isTokenExpired()).isFalse();
    }
}