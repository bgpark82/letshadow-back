package com.bgpark.letshadow.domain.youtube;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class VideoDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void deserialize() throws JsonProcessingException {
        VideoDto.Res video = mapper.readValue(VideoMock.mockVideo, VideoDto.Res.class);

        assertThat(video.getNextPageToken()).isEqualTo("CAEQAA");
        assertThat(video.getTotalResults()).isEqualTo(1487);
        assertThat(video.getResultsPerPage()).isEqualTo(1);
        assertThat(video.getItems().size()).isEqualTo(1);
    }

    @Test
    void deserializeItem() throws JsonProcessingException {
        VideoDto.Res video = mapper.readValue(VideoMock.mockVideo, VideoDto.Res.class);
        VideoDto.Item item = video.getItems().get(0);

        assertThat(item.getId()).isEqualTo("GKoCibDM6Ns");
        assertThat(item.getChannelId()).isEqualTo("UCtV98yyffjUORQRGTuLHomw");
        assertThat(item.getTitle()).isEqualTo("Spring Framework Basic 5강 Ioc컨테이너와 DI(Dependency Injection) | T아카데미");
        assertThat(item.getId()).isNotNull();
        assertThat(item.getPublishedAt()).isEqualTo("2016-11-09T04:10:27Z");
        assertThat(item.getDuration()).isEqualTo("PT20M38S");
        assertThat(item.getChannelTitle()).isEqualTo("SKplanet Tacademy");
        assertThat(item.getCaption()).isEqualTo("false");
        assertThat(item.getThumbnails().get("default").getUrl()).isEqualTo("https://i.ytimg.com/vi/GKoCibDM6Ns/default.jpg");
        assertThat(item.getThumbnails().get("medium").getUrl()).isEqualTo("https://i.ytimg.com/vi/GKoCibDM6Ns/mqdefault.jpg");
        assertThat(item.getThumbnails().get("high").getUrl()).isEqualTo("https://i.ytimg.com/vi/GKoCibDM6Ns/hqdefault.jpg");
        assertThat(item.getThumbnails().get("standard").getUrl()).isEqualTo("https://i.ytimg.com/vi/GKoCibDM6Ns/sddefault.jpg");
    }

    @Test
    void stringToLocalDateTime() {
        String date = "2016-11-09T04:10:27Z";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime parsedDate = LocalDateTime.parse(date, dateTimeFormatter);

        assertThat(parsedDate.getYear()).isEqualTo(2016);
        assertThat(parsedDate.getMonthValue()).isEqualTo(11);
        assertThat(parsedDate.getDayOfMonth()).isEqualTo(9);
    }
};



