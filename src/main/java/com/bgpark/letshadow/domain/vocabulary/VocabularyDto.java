package com.bgpark.letshadow.domain.vocabulary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VocabularyDto {


    @Data
    public static class SearchReq {

        private String word;
        private String videoId;

    }
    @Data
    public static class SearchRes {

        private String word;
        private String translation;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ListReq {

        private String videoId;

    }
    @Data
    public static class ListRes {

        private Vocabulary vocabulary;

    }


}
