package com.felix.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author felix
 */
public class RedisConsumerImpl implements MqConsumer {

    private static final Logger log = LoggerFactory.getLogger(RedisConsumerImpl.class);
    private String name;
    private Jedis jedis;

    public RedisConsumerImpl(String name, Jedis jedis) {
        this.name = name;
        this.jedis = jedis;
    }

    @Override
    public void subscribe(String topic, String tags, ConsumerHandler consumerHandler) throws Exception {


        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                log.debug("====>MQ接收消息, channel={}, message={}", channel, message);
                try {
                    if (!consumerHandler.onConsume(null, null, message)) {
                        log.error("====>MQ消费失败, channel={}, message={}", channel, message);
                    }
                } catch (Exception e) {
                    log.error("====>MQ消费异常:", e);
                }
            }
        };

        Runnable runnable = () -> {
            jedis.subscribe(jedisPubSub, topic);
        };

        new Thread(runnable).start();
    }

    @Override
    public void subscribe(String topic, ConsumerHandler consumerHandler) throws Exception {
        this.subscribe(topic, null, consumerHandler);
    }

    @Override
    public void shutdown() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
