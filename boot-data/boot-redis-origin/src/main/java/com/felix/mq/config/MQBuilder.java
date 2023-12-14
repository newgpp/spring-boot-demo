package com.felix.mq.config;

import com.felix.mq.consumer.MqConsumer;
import com.felix.mq.producer.MqProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Objects;

/**
 * @author felix
 */

@ConfigurationProperties("com.felix.mq")
public class MQBuilder {

    private static final Logger log = LoggerFactory.getLogger(MQBuilder.class);

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
        return producers.stream().filter(x -> Objects.equals(name, x.getName())).findFirst().orElse(null);
    }

    private ProducerConsumerBuilder getConsumerBuilder(String name) {
        return consumers.stream().filter(x -> Objects.equals(name, x.getName())).findFirst().orElse(null);
    }

    public MqProducer buildProducer(String name) {
        ProducerConsumerBuilder producerBuilder = getProducerBuilder(name);
        if (producerBuilder == null) {
            throw new RuntimeException("缺少MQ消费者配置, name=" + name);
        }
        return null;
    }

    public MqConsumer buildConsumer(String name, MqConsumer.ConsumerHandler consumerHandler) {
        ProducerConsumerBuilder consumerBuilder = getConsumerBuilder(name);
        if (consumerBuilder == null) {
            throw new RuntimeException("缺少MQ消费者配置, name=" + name);
        }
        return null;
    }


}
