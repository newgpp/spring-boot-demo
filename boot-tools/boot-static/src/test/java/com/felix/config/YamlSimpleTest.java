package com.felix.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.felix.entity.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;


public class YamlSimpleTest {

    @Test
    public void read_yaml_as_obj_should_success() {
        //given
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //中划线转驼峰
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        InputStream is = YamlSimpleTest.class.getClassLoader().getResourceAsStream("attributes.yaml");
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
    public void write_obj_as_yaml_str_should_success() {
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
    public void write_obj_as_yaml_file_should_success() {
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
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //when
        String path = "F:/data/attributes.yaml";
        File file = new File(path);
        try {
            mapper.writeValue(file, attributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
