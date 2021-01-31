package com.bgpark.letshadow.api;

import com.bgpark.letshadow.domain.token.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class ServerTokenApi {

    @Value("${server.baseUri}")
    private String baseUri;

    public TokenDto.ServerToken getToken(String email, String accessToken) {

        return WebClient.builder()
                .baseUrl(baseUri)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                )).build()
                .post().uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "password")
                        .queryParam("username", email)
                        .queryParam("password", accessToken)
                        .build())
                .headers(httpHeaders -> httpHeaders.setBasicAuth("test", "test"))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.ServerToken.class))
                .block();
    }
}
