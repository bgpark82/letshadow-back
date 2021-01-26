package com.bgpark.letshadow.domain.token;

import com.bgpark.letshadow.api.GoogleTokenApi;
import com.bgpark.letshadow.exception.ApiException;
import com.bgpark.letshadow.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TokenService {

    private final GoogleTokenRepository googleTokenRepository;
    private final GoogleTokenApi googleTokenApi;

    @Transactional
    public String getValidToken(String email) {
        log.info("check user email : {}", email);

        GoogleToken token = googleTokenRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.GOOGLE_TOKEN_NOT_FOUND) );

        if(token.isTokenExpired()) {
            TokenDto.Refresh refresh = googleTokenApi.refreshToken(token.getRefresh_token());
            token.refreshToken(refresh.getAccess_token(), refresh.getExpires_in());
        }

        log.info("google token : {}", token.getAccess_token());

        return token.getAccess_token();
    }

    public void updateGoogleToken(String email, TokenDto.Res googleToken) {
        Optional<GoogleToken> optional = googleTokenRepository.findByEmail(email);
        log.debug("google token from db : {}", optional);

        if(optional.isPresent()) {
            optional.get().setAccess_token(googleToken.getAccess_token());
        } else {
            googleTokenRepository.save(GoogleToken.builder()
                    .email(email)
                    .access_token(googleToken.getAccess_token())
                    .refresh_token(googleToken.getRefresh_token())
                    .expires_in(googleToken.getExpires_in())
                    .build());
        }
    }

}
