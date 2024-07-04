package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author felix
 * @desc some desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Resource
    private ApplicationContext applicationContext;


    @Test
    public void get_bean_should_success() {
        Map<String, ObjectMapper> beansOfType = applicationContext.getBeansOfType(ObjectMapper.class);

        beansOfType.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

}
