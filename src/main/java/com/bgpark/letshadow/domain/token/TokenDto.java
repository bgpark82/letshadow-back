package com.bgpark.letshadow.domain.token;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

public class TokenDto {

    @Data
    public static class Check {
        private String issued_to;
        private String audience;
        private String user_id;
        private String scope;
        private int expires_in;
        private String email;
        private boolean verified_email;
        private String access_type;
    }

    @Data
    @NoArgsConstructor
    public static class Res {
        private String access_token;
        private int expires_in;
        private String scope;
        private String token_type;
        private String id_token;
        private String refresh_token;
    }

    @Getter
    @Setter
    public static class Refresh {
        private String access_token;
        private int expires_in;
    }

    @Data
    @NoArgsConstructor
    public static class Profile {
        private String email;
    }

    @Data
    public static class ServerToken {
        private String access_token;
        private int expires_in;
        private String scope;
        private String token_type;
        private String refresh_token;
    }

    @Data
    public static class User {
        private String name;
        private String picture;
        private String email;
    }
}

