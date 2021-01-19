package com.bgpark.letshadow.domain.youtube;

import com.bgpark.letshadow.domain.token.GoogleToken;
import com.bgpark.letshadow.domain.token.GoogleTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Videos {

    private final GoogleTokenRepository googleTokenRepository;

    @GetMapping("/videos")
    public ResponseEntity<String> videos(Principal principal) {
        String email = principal.getName();
        log.info("email : {}", email);

        GoogleToken token  = googleTokenRepository.findByEmail(email).get();

        String block = WebClient.create().get()
                .uri("https://www.googleapis.com/youtube/v3/videos?part=snippet,player,contentDetails,status,topicDetails,id&myRating=like&access_token=" + token)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token.getAccess_token()))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        return ResponseEntity.ok(block);
    }

}
