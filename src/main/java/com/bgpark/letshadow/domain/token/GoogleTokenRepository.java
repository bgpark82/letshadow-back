package com.bgpark.letshadow.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Long> {
    Optional<GoogleToken> findByEmail(String email);
}
