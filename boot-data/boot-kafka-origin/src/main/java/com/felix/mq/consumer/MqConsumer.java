package com.felix.mq.consumer;

/**
 * @author felix
 */
public interface MqConsumer {

    interface ConsumerListener{
        boolean onConsume(String tag, String key, String body);
    }

    //tags 逗号分隔 a,b,c
    void subscribe(String topic, String tags, ConsumerListener consumerListener) throws Exception;

    void subscribe(String topic, ConsumerListener consumerListener) throws Exception;

    void shutdown();

}
