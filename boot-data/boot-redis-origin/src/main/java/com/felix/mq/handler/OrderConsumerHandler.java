package com.felix.mq.handler;

import com.felix.mq.consumer.MqConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author felix
 */
public class OrderConsumerHandler implements MqConsumer.ConsumerHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumerHandler.class);

    @Override
    public boolean onConsume(String tag, String key, String body) {

        log.info("====>order consumer listen, tag={}, key={}, body={}", tag, key, body);


        return true;
    }
}
