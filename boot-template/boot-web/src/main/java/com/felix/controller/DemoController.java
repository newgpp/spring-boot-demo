package com.felix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author felix
 * @desc some desc
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private ApplicationContext applicationContext;

    @GetMapping("/get/beans")
    public Object getBeans(){

        Map<String, ObjectMapper> beansOfType = applicationContext.getBeansOfType(ObjectMapper.class);

        return beansOfType;

    }
}
