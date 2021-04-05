package com.bgpark.letshadow.domain.word;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Test
    void save() {
        Word word = Word.builder()
                .type(Word.Type.en)
                .translation("단어")
                .word("word")
                .build();

        Word save = wordRepository.save(word);

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(save.getModifiedAt()).isBefore(LocalDateTime.now());
    }
}