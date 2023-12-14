package com.felix.mq.producer;

/**
 * @author felix
 * @desc some desc
 */
public class RedisProducerImpl implements MqProducer{
    @Override
    public void shutdown() {

    }

    @Override
    public boolean send(String key, String body) throws Exception {
        return false;
    }

    @Override
    public boolean send(String tag, String key, String body) throws Exception {
        return false;
    }

    @Override
    public void sendAsync(String tag, String key, String body, Callback callback) {

    }

    @Override
    public void sendAsync(String key, String body, Callback callback) {

    }
}
