package com.bgpark.letshadow.domain.youtube;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void save() {
        Video video = createVideo("vq4t3r", 1, "비디오");

        Video save = videoRepository.save(video);

        assertThat(save.getCount()).isEqualTo(1);
    }

    @Test
    void increaseCount() {

        videoRepository.save(createVideo("vq4t3r", 1, "비디오"));

        Optional<Video> video = videoRepository.findById(1L);

        if (video.isPresent()) {
            final Video present = video.get();
            System.out.println(present);
            present.increaseCount();
            assertThat(present.getCount()).isEqualTo(2);
        }
    }

    private Video createVideo(String videoId, int count, String title) {
        return Video.builder()
                .videoId(videoId)
                .count(count)
                .title(title)
                .build();
    }
}