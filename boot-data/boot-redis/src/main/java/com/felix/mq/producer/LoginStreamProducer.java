package com.felix.mq.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felix.entity.dto.LoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginStreamProducer {

    private static final Logger log = LoggerFactory.getLogger(LoginStreamProducer.class);

    @Value("${redis.stream.login.key:login-events}")
    private String streamLoginKey;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    public RecordId produce(LoginEvent loginEvent) {
        Map<String, String> map = objectMapper.convertValue(loginEvent, new TypeReference<Map<String, String>>() {
        });
        MapRecord<String, String, String> record = StreamRecords.newRecord()
                .ofMap(map)
                .withStreamKey(streamLoginKey);
        RecordId recordId = redisTemplate.opsForStream().add(record);
        if (Objects.isNull(recordId)) {
            log.info("====> error sending event: {}", record);
            return null;
        }
        log.info("redis stream produce, key: {}, record: {}, recordId: {}", streamLoginKey, record, recordId);
        return recordId;
    }

}
