package com.bgpark.letshadow.domain.youtube;

import com.bgpark.letshadow.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class VideoDeserializer extends JsonDeserializer<VideoDto.Res> {

    @Override
    public VideoDto.Res deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        List<VideoDto.Item> items = new ArrayList<>();
        Iterator<JsonNode> itemsNode = node.get("items").iterator();

        while(itemsNode.hasNext()) {
            JsonNode next = itemsNode.next();
            VideoDto.Item item = VideoDto.Item.builder()
                    .id(next.get("id").asText())
                    .publishedAt(next.get("snippet").get("publishedAt").asText())
                    .channelId(next.get("snippet").get("channelId").asText())
                    .title(next.get("snippet").get("title").asText())
                    .description(next.get("snippet").get("description").asText())
                    .channelTitle(next.get("snippet").get("channelTitle").asText())
                    .thumbnails(getThumbnail(next.get("snippet").get("thumbnails")))
                    .duration(next.get("contentDetails").get("duration").asText())
                    .caption(next.get("contentDetails").get("caption").asText())
                    .build();
            items.add(item);
        }

        return VideoDto.Res.builder()
                .nextPageToken(node.get("nextPageToken").asText())
                .totalResults(node.get("pageInfo").get("totalResults").asInt())
                .resultsPerPage(node.get("pageInfo").get("resultsPerPage").asInt())
                .items(items)
                .build();
    }

    public Map<String, VideoDto.Thumbnail> getThumbnail(JsonNode thumbnails) {
        String[] keys = {"default","medium","high","standard","maxres"};
        Map<String, VideoDto.Thumbnail> map = new HashMap<>();
        Arrays.stream(keys)
            .filter(key -> thumbnails.has(key))
            .forEach(key -> {
            JsonNode thumbnail = thumbnails.get(key);
            map.put(key, VideoDto.Thumbnail.builder()
                    .url(thumbnail.get("url").asText())
                    .width(thumbnail.get("width").asInt())
                    .height(thumbnail.get("height").asInt())
                    .build());
        });
        return map;
    }
}
