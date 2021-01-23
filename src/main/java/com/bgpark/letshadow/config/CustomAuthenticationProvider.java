package com.bgpark.letshadow.config;

import com.bgpark.letshadow.domain.token.TokenDto;
import com.bgpark.letshadow.domain.user.User;
import com.bgpark.letshadow.domain.user.UserRepository;
import com.bgpark.letshadow.exception.ApiException;
import com.bgpark.letshadow.exception.CustomAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
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
                restTemplate.postForEntity(GOOGLE_PROVIDER_ENDPOINT + password, null, TokenDto.Check.class);
            } catch (RuntimeException e) {
                throw new ApiException("Invalid Google Token");
            }

            log.debug("email : {}",email);
            log.debug("password : {}",password);
            log.debug("user : {}",user);
            log.debug("authorities : {}",user.getAuthorities());
            user.setPassword(password);
       return new JwtAuthenticationToken(user, password, user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
