package com.utils;

import tools.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String filePath, Class<T> clazz) {
        try {
            return mapper.readValue(new File(filePath), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to " + filePath, e);
        }
    }
}
