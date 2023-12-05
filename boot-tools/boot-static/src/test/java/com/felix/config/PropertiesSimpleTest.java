package com.felix.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesSimpleTest {

    @Test
    public void read_properties_should_success() {
        //given
        Properties prop = new Properties();
        //when
        try (InputStream in = PropertiesSimpleTest.class.getClassLoader().getResourceAsStream("attributes.properties")) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //then
        Assert.assertEquals("a deer, a female deer", prop.getProperty("doe"));
        Assert.assertNotNull(prop);
    }

    @Test
    public void write_properties_should_success() {
        //given
        Properties prop = new Properties();
        prop.setProperty("doe", "a deer, a female deer");
        prop.setProperty("pi", "3.14159");
        prop.setProperty("xmas", "true");
        prop.setProperty("frenchHens", "3");
        prop.setProperty("xmasFifthDay.callingBirds", "a deer, a female deer");
        String path = "F:/data/attributes.properties";
        //when
        try (FileOutputStream os = new FileOutputStream(path)) {
            prop.store(os, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
