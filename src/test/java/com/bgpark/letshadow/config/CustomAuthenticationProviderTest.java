package com.bgpark.letshadow.config;

import com.bgpark.letshadow.domain.token.TokenDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomAuthenticationProviderTest {

    @Autowired private TestRestTemplate restTemplate;

    @Disabled
    @Test
    void checkGoogleToken() {
        String uri = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=ya29.a0AfH6SMBetULK8WL-2zO0zSfQbwiX8cfvwkr7gAjeoLrCBGyNYkvvWwfgWgzCf0udVMuDcrZffZ55-y9oYKXEfddDgFIOxZa6dpfhbsS4z5hNYffNcqisE4deA1kKK3PZTmJNy-pgSkQVwukhs1gAKMWXJoinlO5HY_d_JvXTnjQ";

        ResponseEntity<TokenDto.Check> checkToken  = restTemplate.postForEntity(uri, null, TokenDto.Check.class);
        assertThat(checkToken.getBody().getEmail()).isEqualTo("bgpark82@gmail.com");
    }

    @Disabled
    @Test
    void checkGoogleInvalidToken() {
        String uri = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=invalid_token";

        ResponseEntity<TokenDto.Check> checkToken = restTemplate.postForEntity(uri, null, TokenDto.Check.class);
        assertThat(checkToken.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}