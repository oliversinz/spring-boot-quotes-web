package spring.quotes.web.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;

public class TestClass {

    public static void main(String[] args) throws Exception {

        URL jsonUrl = new URL("https://quotable.io/authors?slug=albert-einstein");

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(jsonUrl);

        jsonNode = jsonNode.findValue("results");

        System.out.println(jsonNode.get(0).get("bio").asText());

    }

}
