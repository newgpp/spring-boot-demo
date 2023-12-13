package com.felix.mq.config;

import com.felix.mq.consumer.MqConsumer;
import com.felix.mq.listener.OrderConsumerListener;
import com.felix.mq.producer.MqProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author felix
 */
@Configuration
@EnableConfigurationProperties(MQBuilder.class)
public class MQConfiguration {

    @Resource
    private MQBuilder mqBuilder;

    @Bean("orderPlaceProducer")
    public MqProducer orderPlaceProducer() {
        return mqBuilder.buildProducer("orderPlaceProducer");
    }

    @Bean("orderConsumerListener")
    public MqConsumer.ConsumerListener orderConsumer() {
        return new OrderConsumerListener();
    }

    @Bean("orderConsumer")
    public MqConsumer orderPlaceConsumer(@Qualifier("orderConsumerListener") MqConsumer.ConsumerListener orderConsumerListener) {
        return mqBuilder.buildConsumer("orderConsumer", orderConsumerListener);
    }
}
