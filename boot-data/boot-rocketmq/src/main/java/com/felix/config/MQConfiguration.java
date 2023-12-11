package com.felix.config;

import com.felix.mq.consumer.ApacheRocketMqConsumerImpl;
import com.felix.mq.consumer.ApacheRocketMqProducerImpl;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author felix
 */
@Configuration
@EnableConfigurationProperties(MQBuilder.class)
public class MQConfiguration {

    @Autowired
    private MQBuilder mqBuilder;

    @Bean("orderPlaceProducer")
    public ApacheRocketMqProducerImpl orderPlaceProducer() {
        MQBuilder.ProducerConsumerBuilder builder = mqBuilder.getProducer("orderPlaceProducer");
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(mqBuilder.getBrokers());
        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setProducerGroup(builder.getGroup());
        return new ApacheRocketMqProducerImpl(builder.getName(), builder.getTopic(), producer);
    }

    @Bean("orderPlaceConsumer")
    public ApacheRocketMqConsumerImpl orderPlaceConsumer() {
        MQBuilder.ProducerConsumerBuilder builder = mqBuilder.getConsumer("orderPlaceConsumer");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(builder.getGroup());
        consumer.setNamesrvAddr(mqBuilder.getBrokers());
        consumer.setConsumerGroup(builder.getGroup());
        consumer.setInstanceName(UUID.randomUUID().toString());
        return new ApacheRocketMqConsumerImpl(builder.getName(), consumer);
    }
}
