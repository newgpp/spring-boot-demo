package com.felix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.felix.entity.Attributes;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author felix
 * @desc some desc
 */
public class JacksonTest {

    /**
     * "doe": "a deer, a female deer",
     *   "pi": 3.14159,
     */
    @Test
    public void object_to_json_should_success(){
        ObjectMapper objectMapper = new ObjectMapper();
        Attributes attributes = new Attributes();
        attributes.setDoe("a deer, a female deer");
        attributes.setPi(new BigDecimal("3.14159"));
        attributes.setXmas(true);
        attributes.setCallingBirds(Arrays.asList("huey", "dewey", "louie", "fred"));
        Attributes.XmasFifthDay xmasFifthDay = new Attributes.XmasFifthDay();
        xmasFifthDay.setCallingBirds("four");
        xmasFifthDay.setFrenchHens(3);
        attributes.setXmasFifthDay(xmasFifthDay);
        try {
            String s = objectMapper.writeValueAsString(attributes);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void json_to_object_should_success(){
        ObjectMapper objectMapper = new ObjectMapper();

        String json = "{\"doe\":\"a deer, a female deer\",\"pi\":3.14159,\"xmas\":true,\"frenchHens\":null,\"callingBirds\":[\"huey\",\"dewey\",\"louie\",\"fred\"],\"xmasFifthDay\":{\"callingBirds\":\"four\",\"frenchHens\":3}}";

        try {
            Attributes attributes = objectMapper.readValue(json, Attributes.class);
            System.out.println(attributes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create_json_object_should_success(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        //when
        objectNode.put("doe", "a deer, a female deer");
        objectNode.put("pi", new BigDecimal("3.14159"));
        objectNode.put("xmas", true);
        objectNode.put("french-hens", 3);

        String s = objectNode.toString();

        System.out.println(s);
    }
}
