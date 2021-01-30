package com.bgpark.letshadow.domain.token;

import com.bgpark.letshadow.api.GoogleTokenApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private AutoCloseable autoCloseable;

    private TokenService tokenService;

    @Mock
    private GoogleTokenRepository googleTokenRepository;
    @Mock
    private GoogleTokenApi googleTokenApi;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        tokenService = new TokenService(googleTokenRepository, googleTokenApi);
    }

    @Test
    void updateIfExist() {
        String email = "popo8682@gmail.com";
        TokenDto.Res request = new TokenDto.Res();
        request.setAccess_token("test_access_token");

        GoogleToken oldToken = GoogleToken.builder()
                .email(email)
                .access_token("test_access_token")
                .build();

        given(googleTokenRepository.findByEmail(email))
                .willReturn(Optional.of(oldToken));

        tokenService.updateGoogleToken(email, request);
        verify(googleTokenRepository, never()).save(any());
    }

    @Test
    void saveIfNonExist() {
        String email = "popo8682@gmail.com";
        TokenDto.Res request = new TokenDto.Res();
        request.setAccess_token("test_access_token");

        given(googleTokenRepository.findByEmail(email))
                .willReturn(Optional.empty());

        tokenService.updateGoogleToken(email, request);

        verify(googleTokenRepository).save(any());
    }

    @AfterEach
    void cleanUp() {
        try {
            autoCloseable.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail to close");
        }
    }
}