package com.felix;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {

    @Test
    public void test_01(){
        Jedis jedis = new Jedis("redis://192.168.197.101:6379");

        jedis.auth("redis123");

        String s = jedis.get("123");
        System.out.println(s);
        System.out.println(s);
    }
}
