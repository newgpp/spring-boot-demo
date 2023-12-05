package com.felix.controller;

import com.felix.entity.dto.LoginEvent;
import com.felix.mq.producer.LoginStreamProducer;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redis/stream")
public class RedisStreamController {

    @Resource
    private LoginStreamProducer loginStreamProducer;


    @PostMapping("/login/xadd")
    public Object login(@RequestBody LoginEvent loginEvent) {

        RecordId recordId = loginStreamProducer.produce(loginEvent);

        return recordId;
    }

}
