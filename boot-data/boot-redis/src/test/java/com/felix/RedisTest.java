package com.felix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author felix
 * @desc some desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void redis_multi_should_success() {
        List<Object> values = getTokenTtl("1");
        System.out.println(values);
    }

    public List<Object> getTokenTtl(String key) {
        SessionCallback<List<Object>> callback = new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) {
                operations.multi();
                operations.opsForValue().get(key);
                operations.getExpire(key, TimeUnit.SECONDS);
                return operations.exec();
            }
        };
        return redisTemplate.execute(callback);
    }
}
