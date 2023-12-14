package com.felix.mq.consumer;

/**
 * @author felix
 */
public interface MqConsumer {

    interface ConsumerHandler {
        boolean onConsume(String tag, String key, String body);
    }

    //tags 逗号分隔 a,b,c
    void subscribe(String topic, String tags, ConsumerHandler consumerHandler) throws Exception;

    void subscribe(String topic, ConsumerHandler consumerHandler) throws Exception;

    void shutdown();

}
