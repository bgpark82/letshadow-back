package com.bgpark.letshadow.config;

import com.bgpark.letshadow.domain.token.TokenDto;
import com.bgpark.letshadow.domain.user.User;
import com.bgpark.letshadow.domain.user.UserRepository;
import com.bgpark.letshadow.exception.CustomAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final String GOOGLE_PROVIDER_ENDPOINT = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            String email = authentication.getName();
            String password = authentication.getCredentials().toString();
            User user = userRepository.findByEmail(email);

            try {
                ResponseEntity<TokenDto.Check> response = restTemplate.postForEntity(GOOGLE_PROVIDER_ENDPOINT + password, null, TokenDto.Check.class);
            } catch (RuntimeException e) {
                throw new CustomAuthException("1001","Invalid Google Token");
            }

            // 비밀번호 확인
            System.out.println(email);
            System.out.println(password);
            System.out.println(user);
            System.out.println(user.getAuthorities());
            user.setPassword(password);
       return new JwtAuthenticationToken(user, password, user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
