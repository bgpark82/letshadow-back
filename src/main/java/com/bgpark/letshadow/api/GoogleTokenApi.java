package com.bgpark.letshadow.api;

import com.bgpark.letshadow.domain.token.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component
public class GoogleTokenApi {

    @Value("${server.redirectUri}")
    private String serverRedirectUri;

    private static final String GOOGLE_CALLBACK_URI = "https://oauth2.googleapis.com";
    private static final String GOOGLE_TOKEN_INFO_URI = "https://www.googleapis.com/oauth2/v1/tokeninfo";

    private static final String CLIENT_ID = "758204078687-dhoc57phmqfj5epv6vvi327kguumm9p8.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "vRDY1-vBeRsXstnZlrYqrgGF";
    private static final String GRANT_TYPE = "authorization_code";

    public TokenDto.Res getToken(String code) {

        log.debug("server redirect uri : {}", serverRedirectUri);

        return WebClient.builder()
                .baseUrl(GOOGLE_CALLBACK_URI)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                ))
                .build()
                .post().uri(uriBuilder -> uriBuilder
                        .path("/token")
                        .queryParam("code", code)
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("client_secret", CLIENT_SECRET)
                        .queryParam("grant_type", GRANT_TYPE)
                        .queryParam("redirect_uri", serverRedirectUri).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.Res.class)).block();
    }

    public TokenDto.Profile getTokenInfo(String accessToken) {

        return WebClient.builder()
                .baseUrl(GOOGLE_TOKEN_INFO_URI)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                )).build()
                .post().uri(uriBuilder -> uriBuilder
                        .queryParam("access_token", accessToken)
                        .build())
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.Profile.class))
                .block();
    }
}
