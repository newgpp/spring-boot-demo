package com.felix.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.felix.entity.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class JsonSimpleTest {

    @Test
    public void read_json_as_obj_should_success() {
        //given
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        //中划线转驼峰
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        InputStream is = JsonSimpleTest.class.getClassLoader().getResourceAsStream("attributes.json");
        Attributes attributes = null;
        try {
            attributes = mapper.readValue(is, Attributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //then
        Assert.assertNotNull(attributes);
    }


    @Test
    public void write_json_as_yaml_str_should_success() {
        //given
        Attributes attributes = new Attributes();
        attributes.setDoe("a deer, a female deer");
        attributes.setPi(new BigDecimal("3.14159"));
        attributes.setXmas(true);
        attributes.setCallingBirds(Arrays.asList("huey", "dewey", "louie", "fred"));
        Attributes.XmasFifthDay xmasFifthDay = new Attributes.XmasFifthDay();
        xmasFifthDay.setCallingBirds("four");
        xmasFifthDay.setFrenchHens(3);
        attributes.setXmasFifthDay(xmasFifthDay);
        //中划线转驼峰
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        String content = null;
        try {
            content = mapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //then
        Assert.assertNotNull(content);
    }

    @Test
    public void write_obj_as_json_file_should_success() {
        //given
        Attributes attributes = new Attributes();
        attributes.setDoe("a deer, a female deer");
        attributes.setPi(new BigDecimal("3.14159"));
        attributes.setXmas(true);
        attributes.setFrenchHens(3);
        attributes.setCallingBirds(Arrays.asList("huey", "dewey", "louie", "fred"));
        Attributes.XmasFifthDay xmasFifthDay = new Attributes.XmasFifthDay();
        xmasFifthDay.setCallingBirds("four");
        xmasFifthDay.setFrenchHens(3);
        attributes.setXmasFifthDay(xmasFifthDay);
        //中划线转驼峰
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        String path = "F:/data/attributes.json";
        File file = new File(path);
        try {
            mapper.writeValue(file, attributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void json_node_read_should_success() {
        //given
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        //中划线转驼峰
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        InputStream is = JsonSimpleTest.class.getClassLoader().getResourceAsStream("attributes.json");
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //then
        Assert.assertNotNull(jsonNode);
        //文本
        Assert.assertEquals("a deer, a female deer", jsonNode.get("doe").asText());
        //数值
        Assert.assertEquals(3, jsonNode.get("french-hens").asInt());
        //按路径获取属性
        Assert.assertEquals("four", jsonNode.at("/xmas-fifth-day/calling-birds").asText());
        //ARRAY
        Assert.assertEquals("huey", jsonNode.get("calling-birds").get(0).asText());
        //缺少属性
        Assert.assertEquals(JsonNodeType.MISSING, jsonNode.at("/a/a/a").getNodeType());
    }

    @Test
    public void json_node_write_should_success() {
        //given
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        //when
        objectNode.put("doe", "a deer, a female deer");
        objectNode.put("pi", new BigDecimal("3.14159"));
        objectNode.put("xmas", true);
        objectNode.put("french-hens", 3);
        //数组
        ArrayNode birds = mapper.createArrayNode();
        birds.add("huey").add("dewey").add("louie").add("fred");
        objectNode.set("calling-birds", birds);
        //对象
        ObjectNode day = mapper.createObjectNode();
        day.put("calling-birds", "four").put("french-hens", 3);
        objectNode.set("xmas-fifth-day", day);
        //then
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(jsonStr);
    }

    @Test
    public void json_array_read_should_success() {
        //given
        String jsonStr = "[{\"parkName\":\"Jane Park\", \"parkId\":\"P10001\"},{\"parkName\":\"Alex Park\", \"parkId\":\"P10002\"},{\"parkName\":\"Mountain Park\", \"parkId\":\"P10003\"}]";
        //when
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> objectNodes = null;
        try {
            objectNodes = mapper.readValue(jsonStr, new TypeReference<List<ObjectNode>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //then
        Assert.assertNotNull(objectNodes);
        Assert.assertEquals(3, objectNodes.size());
    }

}
