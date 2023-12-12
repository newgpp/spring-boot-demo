package com.felix.mq.config;

import com.felix.mq.consumer.ApacheRocketMqConsumerImpl;
import com.felix.mq.consumer.MqConsumer;
import com.felix.mq.producer.ApacheRocketMqProducerImpl;
import com.felix.mq.producer.MqProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author felix
 */

@ConfigurationProperties("com.felix.mq")
public class MQBuilder {

    private String brokers;

    private String ak;

    private String sk;

    private List<ProducerConsumerBuilder> producers;

    private List<ProducerConsumerBuilder> consumers;

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public List<ProducerConsumerBuilder> getProducers() {
        return producers;
    }

    public void setProducers(List<ProducerConsumerBuilder> producers) {
        this.producers = producers;
    }

    public List<ProducerConsumerBuilder> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ProducerConsumerBuilder> consumers) {
        this.consumers = consumers;
    }

    static class ProducerConsumerBuilder {

        private String name;
        private String topic;
        private String tag;
        private String group;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    private ProducerConsumerBuilder getProducerBuilder(String name) {
        return producers.stream().filter(x -> Objects.equals(name, x.getName()))
                .findFirst().orElse(null);
    }

    private ProducerConsumerBuilder getConsumerBuilder(String name) {
        return consumers.stream().filter(x -> Objects.equals(name, x.getName()))
                .findFirst().orElse(null);
    }

    public MqProducer buildProducer(String name) {
        MQBuilder.ProducerConsumerBuilder producerBuilder = getProducerBuilder(name);
        DefaultMQProducer producer;
        if (StringUtils.isNoneEmpty(ak) && StringUtils.isNoneEmpty(sk)) {
            AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(ak, sk));
            producer = new DefaultMQProducer(aclClientRPCHook);
        } else {
            producer = new DefaultMQProducer();
        }
        producer.setNamesrvAddr(brokers);
        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setProducerGroup(producerBuilder.getGroup());
        try {
            producer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new ApacheRocketMqProducerImpl(producerBuilder.getName(), producerBuilder.getTopic(), producer);
    }

    public MqConsumer buildConsumer(String name, MqConsumer.ConsumerListener consumerListener) {
        MQBuilder.ProducerConsumerBuilder consumerBuilder = getConsumerBuilder(name);
        DefaultMQPushConsumer consumer;
        if (StringUtils.isNoneEmpty(ak) && StringUtils.isNoneEmpty(sk)) {
            AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(ak, sk));
            consumer = new DefaultMQPushConsumer(aclClientRPCHook);
        } else {
            consumer = new DefaultMQPushConsumer();
        }
        consumer.setNamesrvAddr(brokers);
        consumer.setConsumerGroup(consumerBuilder.getGroup());
        consumer.setInstanceName(UUID.randomUUID().toString());

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        ApacheRocketMqConsumerImpl mqConsumer = new ApacheRocketMqConsumerImpl(consumerBuilder.getName(), consumer);
        try {
            mqConsumer.subscribe(consumerBuilder.getTopic(), consumerBuilder.getTag(), consumerListener);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return mqConsumer;
    }


}
