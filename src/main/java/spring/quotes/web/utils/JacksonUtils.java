package spring.quotes.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JacksonUtils {

    private static final ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        return defaultObjectMapper;
    }

    public static JsonNode parseJson(String source) throws JsonProcessingException {
        return objectMapper.readTree(source);
    }

    public static JsonNode parseJson(File source) throws IOException {
        return objectMapper.readTree(source);
    }

    public static JsonNode parseJson(URL source) throws IOException {
        return objectMapper.readTree(source);
    }

    public static <T> T jsonToObject(JsonNode node, Class<T> clazz) throws IllegalArgumentException, JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

}
