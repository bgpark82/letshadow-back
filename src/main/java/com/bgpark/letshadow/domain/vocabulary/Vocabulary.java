package com.bgpark.letshadow.domain.vocabulary;

import com.bgpark.letshadow.domain.word.Word;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Vocabulary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoId;

    private Long userId;

    @OneToMany
    private List<Word> words = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public Vocabulary(Long id, String videoId, Long userId, List<Word> words) {
        this.id = id;
        this.videoId = videoId;
        this.userId = userId;
        this.words = words;
    }
}
