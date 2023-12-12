package com.felix.config;

import com.felix.mq.MqConsumer;
import com.felix.mq.consumer.ApacheRocketMqConsumerImpl;
import com.felix.mq.consumer.OrderConsumerListener;
import com.felix.mq.producer.ApacheRocketMqProducerImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public ApacheRocketMqProducerImpl orderPlaceProducer() throws Exception {
        MQBuilder.ProducerConsumerBuilder producerBuilder = mqBuilder.getProducer("orderPlaceProducer");
        String ak = mqBuilder.getAk();
        String sk = mqBuilder.getSk();
        DefaultMQProducer producer;
        if (StringUtils.isNoneEmpty(ak) && StringUtils.isNoneEmpty(sk)) {
            AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(ak, sk));
            producer = new DefaultMQProducer(aclClientRPCHook);
        } else {
            producer = new DefaultMQProducer();
        }
        producer.setNamesrvAddr(mqBuilder.getBrokers());
        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setProducerGroup(producerBuilder.getGroup());
        producer.start();
        return new ApacheRocketMqProducerImpl(producerBuilder.getName(), producerBuilder.getTopic(), producer);
    }

    @Bean("orderConsumerListener")
    public MqConsumer.ConsumerListener orderConsumer() {
        return new OrderConsumerListener();
    }

    @Bean("orderConsumer")
    public ApacheRocketMqConsumerImpl orderPlaceConsumer(@Qualifier("orderConsumerListener") MqConsumer.ConsumerListener orderConsumerListener) throws Exception {
        MQBuilder.ProducerConsumerBuilder consumerBuilder = mqBuilder.getConsumer("orderConsumer");
        String ak = mqBuilder.getAk();
        String sk = mqBuilder.getSk();
        DefaultMQPushConsumer consumer;
        if (StringUtils.isNoneEmpty(ak) && StringUtils.isNoneEmpty(sk)) {
            AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(ak, sk));
            consumer = new DefaultMQPushConsumer(aclClientRPCHook);
        } else {
            consumer = new DefaultMQPushConsumer();
        }
        consumer.setNamesrvAddr(mqBuilder.getBrokers());
        consumer.setConsumerGroup(consumerBuilder.getGroup());
        consumer.setInstanceName(UUID.randomUUID().toString());

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        ApacheRocketMqConsumerImpl mqConsumer = new ApacheRocketMqConsumerImpl(consumerBuilder.getName(), consumer);
        mqConsumer.subscribe(consumerBuilder.getTopic(), consumerBuilder.getTag(), orderConsumerListener);
        return mqConsumer;
    }
}
