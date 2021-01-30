package com.bgpark.letshadow.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtils {

    private static final String YOUTUBE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(YOUTUBE_DATE_FORMAT));
    }

}
