package com.bgpark.letshadow.domain.vocabulary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    Optional<Vocabulary> findByVideoId(String videoId);
}
