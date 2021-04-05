package com.bgpark.letshadow.domain.youtube;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoId;

    private String title;

    private int count;

    @Builder
    public Video(Long id, String videoId, String title, int count) {
        this.id = id;
        this.videoId = videoId;
        this.title = title;
        this.count = count;
    }

    public void increaseCount() {
        this.count = this.count + 1;
    }
}
