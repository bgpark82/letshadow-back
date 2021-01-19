package com.bgpark.letshadow.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Controller
@RequiredArgsConstructor
public class TokenController {

    private final WebClient webClient;
    private final GoogleTokenRepository googleTokenRepository;

    private static final String GOOGLE_CALLBACK_URI = "https://oauth2.googleapis.com";
    private static final String CLIENT_ID = "758204078687-dhoc57phmqfj5epv6vvi327kguumm9p8.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "vRDY1-vBeRsXstnZlrYqrgGF";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/callback";

//    http://localhost:5500/?state=pass-through+value&code=4%2F0AY0e-g7Zwc1Lyc9nmDeAp0p3oKRHjqHepI6rRU7XY0W-mMrXOaAI6-52hLGv6XkJTbxOGA&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube&authuser=0&prompt=consent#

    @GetMapping("/oauth/callback")
    public String callback(@RequestParam(name = "code", required = false) String code) {
//        if(code == null) return "redirect:http://localhost:5500";
        System.out.println(code);

        TokenDto.Res googleToken = WebClient.builder()
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
                        .queryParam("redirect_uri", REDIRECT_URI).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.Res.class)).block();

        // token 확인 -> email
        TokenDto.Profile email = WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v1/tokeninfo")
                                .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create().wiretap(true)
                )).build()
                .post().uri(uriBuilder -> uriBuilder
                .queryParam("access_token", googleToken.getAccess_token())
                .build())
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.Profile.class))
                .block();

        System.out.println(email);

        if(!googleTokenRepository.findByEmail(email.getEmail()).isPresent()) {
            googleTokenRepository.save(GoogleToken.builder()
                    .email(email.getEmail())
                    .access_token(googleToken.getAccess_token())
                    .refresh_token(googleToken.getRefresh_token())
                    .build());
        }

        // email.token -> token 발급
        TokenDto.ServerToken serverToken = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                )).build()
                .post().uri(uriBuilder -> uriBuilder.path("/oauth/token")
                .queryParam("grant_type", "password")
                .queryParam("username", email.getEmail())
                .queryParam("password", googleToken.getAccess_token())
                .build())
                .headers(httpHeaders -> httpHeaders.setBasicAuth("test", "test"))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TokenDto.ServerToken.class))
                .block();

        System.out.println(serverToken);

         return "redirect:http://localhost:5500?access_token=" + serverToken.getAccess_token() + "&refresh_token=" + serverToken.getRefresh_token() + "&expires_in=" +serverToken.getExpires_in() ;

    }
}
