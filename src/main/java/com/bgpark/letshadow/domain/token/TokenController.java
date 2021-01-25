package com.bgpark.letshadow.domain.token;

import com.bgpark.letshadow.api.GoogleTokenApi;
import com.bgpark.letshadow.api.ServerTokenApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final GoogleTokenApi googleTokenApi;
    private final ServerTokenApi serverTokenApi;

    @Value("${client.baseUri}")
    private String clientBaseUri;

    @Transactional
    @GetMapping("/oauth/callback")
    public String callback(@RequestParam(name = "code", required = false) String code) {
        if(code == null) return "redirect:" + clientBaseUri;
        log.debug("code : {}", code);

        TokenDto.Res googleToken = googleTokenApi.getToken(code);
        log.debug("google token : {}",googleToken);

        TokenDto.Profile tokenInfo = googleTokenApi.getTokenInfo(googleToken.getAccess_token());
        log.info("tokenInfo : {}",tokenInfo);

        tokenService.updateGoogleToken(tokenInfo.getEmail(), googleToken);

        TokenDto.ServerToken serverToken = serverTokenApi.getToken(tokenInfo.getEmail(), googleToken.getAccess_token());
        log.info("server token : {}",serverToken);

         return "redirect:" + clientBaseUri +
                 "?access_token=" + serverToken.getAccess_token() +
                 "&refresh_token=" + serverToken.getRefresh_token() +
                 "&expires_in=" +serverToken.getExpires_in() ;

    }
}
