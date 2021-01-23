package com.bgpark.letshadow.domain.token;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenControllerTest {

    private static final String GOOGLE_CALLBACK_URI = "https://oauth2.googleapis.com/token";
    private static final String CODE = "4%2F0AY0e-g5_AtUq8ImI2SaZDawsTLfKDaOOYTlzDtDnh93Fl2ygywb6zNQeO402WeNq-WcqTg";
    private static final String CLIENT_ID = "758204078687-dhoc57phmqfj5epv6vvi327kguumm9p8.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "vRDY1-vBeRsXstnZlrYqrgGF";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:5500";

    @Autowired
    private WebTestClient webTestClient;

    @Disabled
    @Test
    void callback() {
        webTestClient.get()
                .uri("http://localhost:8080/oauth/callback?state=pass-through+value&code=4%2F0AY0e-g5_AtUq8ImI2SaZDawsTLfKDaOOYTlzDtDnh93Fl2ygywb6zNQeO402WeNq-WcqTg&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube&authuser=0&prompt=consent#")
                .exchange()
                .expectStatus().isOk();

    }

    @Disabled
    @Test
    void getGoogleToken() {
        webTestClient.post().uri(uriBuilder ->
            uriBuilder.host(GOOGLE_CALLBACK_URI)
                    .queryParam("code",CODE)
                    .queryParam("client_id",CLIENT_ID)
                    .queryParam("client_secret",CLIENT_SECRET)
                    .queryParam("grant_type",GRANT_TYPE)
                    .queryParam("redirect_uri",REDIRECT_URI).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchange()
                .expectStatus().isOk();

    }
}