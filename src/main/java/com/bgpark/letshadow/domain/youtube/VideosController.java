package com.bgpark.letshadow.domain.youtube;


import com.bgpark.letshadow.domain.token.GoogleToken;
import com.bgpark.letshadow.domain.token.GoogleTokenRepository;
import com.bgpark.letshadow.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideosController {

    private final GoogleTokenRepository googleTokenRepository;

    @GetMapping("/videos")
    public ResponseEntity<String> videos(Principal principal) {
        String email = principal.getName();
        log.info("email : {}", email);

        GoogleToken token = googleTokenRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("토큰이 존재하지 않습니다") );
        log.info("google token : {}", token.getAccess_token());

        try {
            String block = WebClient.create().get()
                    .uri("https://www.googleapis.com/youtube/v3/videos?part=snippet,player,contentDetails,status,topicDetails,id&myRating=like&access_token=" + token)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(token.getAccess_token()))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                        Mono<String> error = clientResponse.bodyToMono(String.class);
                        log.error("client authorization error : {}", error);
                        return error.flatMap(message -> {
                            throw new ApiException(message);
                        });
                    })
                    .bodyToMono(String.class)
                    .block();


            return ResponseEntity.ok(block);
        } catch (RuntimeException e) {

            WebClient.create()
                    .post()
                    .uri(uriBuilder -> uriBuilder
                        .host("https://www.googleapis.com/oauth2/v1/tokeninfo")
                        .queryParam("access_token",token)
                        .build()
                    ).exchangeToMono(clientResponse -> {
                        log.info("google token info status : {}", clientResponse.statusCode());
                        return null;
                    })
                    .block();

            log.error("google oauth error : {}", e.getMessage());
            throw new ApiException("유효하지 않은 구글 토큰입니다",400);
        }
    }
}
