package com.bgpark.letshadow.config;

import com.bgpark.letshadow.domain.User;
import com.bgpark.letshadow.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final String GOOGLE_PROVIDER_ENDPOINT = "https://www.googleapis.com/oauth2/v1/tokeninfo";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(email);

        // 사용자 존재 여부 확인
//        HttpResponse<JsonNode> response = Unirest.get(GOOGLE_PROVIDER_ENDPOINT)
//                .queryString("access_token", accessToken).asJson();
//        ResponseData res = objectMapper.readValue(response.getBody().toString(), ResponseData.class);

        // 비밀번호 확인
        System.out.println(email);
        System.out.println(password);
        System.out.println(user);
        System.out.println(user.getAuthorities());
        return new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
