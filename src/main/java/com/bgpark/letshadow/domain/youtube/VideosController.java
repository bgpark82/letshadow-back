package com.bgpark.letshadow.domain.youtube;


import com.bgpark.letshadow.domain.token.TokenService;
import com.bgpark.letshadow.exception.ApiException;
import com.bgpark.letshadow.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideosController {

    private final TokenService tokenService;

    @Transactional
    @GetMapping("/videos")
    public ResponseEntity<VideoDto.Res> videos(Principal principal) {

        String token = tokenService.getValidToken(principal.getName());

        try {
            VideoDto.Res block = WebClient.create().get()
                    .uri("https://www.googleapis.com/youtube/v3/videos?part=snippet,player,contentDetails,status,topicDetails,id&myRating=like&access_token=" + token + "&maxResults=30")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                    .retrieve()
//                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//                        // TODO: 403인 경우 구글에서 에러 (google oauth error : 403 Forbidden from GET)
//                        Mono<String> error = clientResponse.bodyToMono(String.class);
//                        log.error("client authorization error : {}", error);
//                        return error.flatMap(message -> {
//                            throw new ApiException(ErrorCode.GOOGLE_TOKEN_EXPIRED);
//                        });
//                    })
                    .bodyToMono(VideoDto.Res.class)
                    .block();
            return ResponseEntity.ok(block);
        } catch (RuntimeException e) {
            log.error("google oauth error : {}", e.getMessage());
            throw new ApiException(ErrorCode.GOOGLE_TOKEN_EXPIRED);
        }
    }
}
