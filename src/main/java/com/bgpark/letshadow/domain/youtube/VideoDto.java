package com.bgpark.letshadow.domain.youtube;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class VideoDto {


    @JsonDeserialize(using = VideoDeserializer.class)
    @Getter
    @Builder
    public static class Res {
        private String nextPageToken;
        private int totalResults;
        private int resultsPerPage;
        private List<Item> items;
    }

    @Getter
    public static class Item {
        private String id;
        private String publishedAt;
        private String channelId;
        private String title;
        private String description;
        private String channelTitle;
        private String duration;
        private String caption;
        private Map<String, Thumbnail> thumbnails;

        @Builder
        public Item(String id, String publishedAt, String channelId, String title, String description, String channelTitle, String duration, String caption, Map<String, Thumbnail> thumbnails) {
            this.id = id;
            this.publishedAt = publishedAt;
            this.channelId = channelId;
            this.title = title;
            this.description = description;
            this.channelTitle = channelTitle;
            this.duration = duration;
            this.caption = caption;
            this.thumbnails = thumbnails;
        }
    }

    @Builder
    @Getter
    public static class Thumbnail {
        private String url;
        private int width;
        private int height;
    }
}
