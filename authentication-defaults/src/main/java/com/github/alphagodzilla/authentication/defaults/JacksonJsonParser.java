package com.github.alphagodzilla.authentication.defaults;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alphagodzilla.authentication.core.JsonParser;

/**
 * @author AlphaGodzilla
 * @date 2022/3/14 18:21
 */
public class JacksonJsonParser implements JsonParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T toBean(String jsonStr, Class<T> tClass) {
        try {
            return objectMapper.readValue(jsonStr,tClass);
        } catch (JsonProcessingException exception) {
            throw new JsonParseException(exception);
        }
    }

    @Override
    public String toJsonString(Object jsonObject) {
        try {
            return objectMapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException exception) {
            throw new JsonParseException(exception);
        }
    }
}
