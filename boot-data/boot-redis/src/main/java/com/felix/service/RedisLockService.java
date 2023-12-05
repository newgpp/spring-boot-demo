package com.felix.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockService {

    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 加锁
     *
     * @param key     redis主键
     * @param value   值
     * @param seconds 过期时间
     */
    public boolean lock(String key, String value, long seconds) {
        final boolean result = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS));
        if (result) {
            log.info("[redisTemplate redis]设置锁缓存 缓存 {} ========缓存时间为{}秒", key, seconds);
        }
        return result;
    }

    /**
     * 解锁
     *
     * @param key   redis主键
     * @param value 值
     */
    public boolean unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        if (Objects.equals(1L, result)) {
            log.info("[redisTemplate redis]释放锁 缓存  {}", key);
            return true;
        }
        return false;
    }

}
