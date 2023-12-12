package com.felix.mq.listener;

import com.felix.mq.consumer.MqConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author felix
 * @desc some desc
 */
public class OrderConsumerListener implements MqConsumer.ConsumerListener {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumerListener.class);

    @Override
    public boolean onConsume(String tag, String key, String body) {

        log.info("====>order consumer listen, tag={}, key={}, body={}", tag, key, body);


        return true;
    }
}
