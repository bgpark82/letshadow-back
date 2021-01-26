package com.bgpark.letshadow.domain.token;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Setter
    private String access_token;
    @Column(updatable = false)
    private String refresh_token;
    private int expires_in;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

//    {
//        "access_token": "ya29.a0AfH6SMDz9E2t1JsrQShsdKvZmY1emNAQY6OLZLEbSPc-xcUShw861c3OJz0kTVWnmbaiifU7nS-UEQJ6XweTLHiq8-dy7Zm2IRfy95HvPQBmj5c7oyCGyld6ez5Ha4XGXmuYftAG3Tc_c7jaJFOvrHXZLjToiWEJO45iwQqSHkk",
//            "expires_in": 3599,
//            "scope": "openid https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/userinfo.profile",
//            "token_type": "Bearer",
//            "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImVlYTFiMWY0MjgwN2E4Y2MxMzZhMDNhM2MxNmQyOWRiODI5NmRhZjAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI3NTgyMDQwNzg2ODctZGhvYzU3cGhtcWZqNWVwdjZ2dmkzMjdrZ3V1bW05cDguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI3NTgyMDQwNzg2ODctZGhvYzU3cGhtcWZqNWVwdjZ2dmkzMjdrZ3V1bW05cDguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQwNDY2Mzc5NDI4MDkzMDc1NTEiLCJlbWFpbCI6InBvcG84NjgyQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiQzJjTW5weEoxNTZjWjZIU3pIMWNWQSIsIm5hbWUiOiJCeWVvbmdnaWwgUGFyayIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS0vQU9oMTRHaS1Fc0ZNbDg1Q3FDYng4THpCZ1BCT1NNeGt1WmdBU3VySmp5SnF4SUk9czk2LWMiLCJnaXZlbl9uYW1lIjoiQnllb25nZ2lsIiwiZmFtaWx5X25hbWUiOiJQYXJrIiwibG9jYWxlIjoiZW4iLCJpYXQiOjE2MTE2NzI0MDQsImV4cCI6MTYxMTY3NjAwNH0.CG_TdVoP-sY_E9sEuGKbIqSMz6v2jDQPrWEO3ltn53J4ZrWWM5I6UZAGiUc09zlMPJgJ4myitbZswztyMjyDr81_dlPC4OdZzB1k2DnjEG6Sk5QoNb1gin159vgmlDYs-pIzIHKeHAE7aTuPpEB7n8uilY9qw13GXP8-26uB4h2r5rfTaKpuiBSHWSTfRIU2QeDaCg1oWcvsBKxCGcIlUO0nfpPVD5YHsnl8-ocNz5LU5BzN0hcO2dEuI_-BqcqOGIkPiPKuudv394wVaxqod5Tr586RXg1xZr82xe5qIUx8R1WWRKXWX5_13HwbMCN4EEsAe0HbZnedcj8-myReDQ"
//    }

    @Builder
    public GoogleToken(String email, String access_token, String refresh_token, int expires_in) {
        this.email = email;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }

    @Builder
    public GoogleToken(String email, String access_token, String refresh_token, int expires_in, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.email = email;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void refreshToken(String access_token, int expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public boolean isTokenExpired() {
        return modifiedAt.plusSeconds(expires_in)
                .isBefore(LocalDateTime.now());
    }
}
