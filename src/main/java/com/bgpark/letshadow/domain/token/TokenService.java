package com.bgpark.letshadow.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final GoogleTokenRepository googleTokenRepository;

    public void updateGoogleToken(String email, TokenDto.Res googleToken) {
        Optional<GoogleToken> optional = googleTokenRepository.findByEmail(email);
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
