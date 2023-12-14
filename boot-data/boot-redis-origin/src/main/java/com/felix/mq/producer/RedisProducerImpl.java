package com.felix.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author felix
 * @desc some desc
 */
public class RedisProducerImpl implements MqProducer {

    private static final Logger log = LoggerFactory.getLogger(RedisProducerImpl.class);

    private String name;
    private String topic;
    private Jedis jedis;

    public RedisProducerImpl(String name, String topic, Jedis jedis) {
        this.name = name;
        this.topic = topic;
        this.jedis = jedis;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean send(String key, String body) throws Exception {
        return this.send(null, key, body);
    }

    @Override
    public boolean send(String tag, String key, String body) throws Exception {
        long publish = jedis.publish(topic, body);
        log.info("=====>MQ发送消息, channel={}, body={}, publish={}", topic, body, publish);
        return true;
    }

    @Override
    public void sendAsync(String tag, String key, String body, Callback callback) {
        jedis.publish(topic, body);
    }

    @Override
    public void sendAsync(String key, String body, Callback callback) {
        this.sendAsync(null, key, body, callback);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
