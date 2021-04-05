package com.bgpark.letshadow.domain.vocabulary;

import com.bgpark.letshadow.domain.word.Word;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VocabularyRepositoryTest {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Test
    void save() {
        Word word = createWord("단어", "word");
        Vocabulary vocabulary = createVocabulary(1L, "abc", word);

        Vocabulary save = vocabularyRepository.save(vocabulary);

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(save.getModifiedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void createVocabulary() {
        Word word = createWord("단어", "word");
        Vocabulary vocabulary = createVocabulary(1L, "abc", word);
        vocabularyRepository.save(vocabulary);

        Optional<Vocabulary> newVocabulary = vocabularyRepository.findByVideoId("abc");

        if(newVocabulary.isPresent()) {
            Vocabulary present = newVocabulary.get();
            assertThat(present.getWords().size()).isEqualTo(1);
        }

    }

    private Vocabulary createVocabulary(Long userId, String videoId, Word word) {
        List<Word> words = Arrays.asList(word);

        return Vocabulary.builder()
                .userId(userId)
                .videoId(videoId)
                .words(words)
                .build();
    }

    private Word createWord(String translation, String word) {
        return Word.builder()
                .type(Word.Type.en)
                .translation(translation)
                .word(word)
                .build();
    }


}