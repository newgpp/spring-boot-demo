package com.felix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redis/ops")
public class RedisOpsController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/set")
    public Object setValue(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        Object o = redisTemplate.opsForValue().get(key);
        return new ObjectMapper().createObjectNode().put(key, o == null ? null :o.toString());
    }

    @GetMapping("/get")
    public Object setValue(@RequestParam("key") String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return new ObjectMapper().createObjectNode().put(key, o == null ? null :o.toString());
    }


}
