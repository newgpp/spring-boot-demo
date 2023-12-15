package com.felix;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisTest {

    @Test
    public void strings_get_set_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        jedis.set("events/city/rome", "32,15,223,828");
        String s = jedis.get("events/city/rome");
        //then
        System.out.println(s);
    }

    @Test
    public void lists_push_pop_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        String key = "queue#tasks";
        jedis.lpush(key, "firstTask");
        jedis.lpush(key, "secondTask");
        //then
        long len = jedis.llen(key);
        System.out.println(len);
        List<String> list = jedis.lrange(key, 0, len - 1);
        System.out.println(list);
        String task = jedis.rpop(key);
        System.out.println(task);
    }

    @Test
    public void sets_add_get_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        String key = "nicknames";
        jedis.sadd(key, "nickname#1");
        jedis.sadd(key, "nickname#2");
        jedis.sadd(key, "nickname#1");
        //then
        Set<String> nicknames = jedis.smembers(key);
        System.out.println(nicknames);
        boolean exists = jedis.sismember(key, "nickname#1");
        System.out.println(exists);
    }

    @Test
    public void hashs_set_get_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        String key = "user#1";
        jedis.hset(key, "name", "Peter");
        jedis.hset(key, "job", "politician");
        //then
        String name = jedis.hget(key, "name");
        System.out.println(name);
        Map<String, String> fields = jedis.hgetAll(key);
        System.out.println(fields);
    }

    @Test
    public void sorted_set_rank_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        String key = "ranking";
        jedis.zadd(key, 3000.0, "PlayerOne");
        jedis.zadd(key, 1500.0, "PlayerTwo");
        jedis.zadd(key, 8200.0, "PlayerThree");
        //then
        List<String> list = jedis.zrange(key, 0, Integer.MAX_VALUE);
        System.out.println(list);
        Long rank = jedis.zrank(key, "PlayerThree");
        System.out.println(rank);
    }

    @Test
    public void multi_command_should_success() {
        //given
        String url = "redis://192.168.159.111:6379";
        Jedis jedis = new Jedis(url);
        jedis.auth("redis123");
        //when
        String key = "token";
        jedis.setex(key, 10, String.valueOf(System.currentTimeMillis()));
        Transaction multi = jedis.multi();
        multi.get(key);
        multi.ttl(key);
        List<Object> res = multi.exec();
        System.out.println(res);
    }

    @Test
    public void connection_pool_should_success() {
        //given
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        //when
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.159.111", 6379, null, "redis123");
        //then
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("events/city/rome", "32,15,223,828");
            String s = jedis.get("events/city/rome");
            System.out.println(s);
        }
    }


}
