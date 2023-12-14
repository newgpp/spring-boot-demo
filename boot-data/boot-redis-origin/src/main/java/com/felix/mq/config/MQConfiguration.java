package com.felix.mq.config;

import com.felix.mq.consumer.MqConsumer;
import com.felix.mq.handler.OrderConsumerHandler;
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

    @Bean("orderConsumerHandler")
    public MqConsumer.ConsumerHandler orderConsumerHandler() {
        return new OrderConsumerHandler();
    }

    @Bean("orderConsumer")
    public MqConsumer orderPlaceConsumer(@Qualifier("orderConsumerHandler") MqConsumer.ConsumerHandler orderConsumerHandler) {
        return mqBuilder.buildConsumer("orderConsumer", orderConsumerHandler);
    }
}
