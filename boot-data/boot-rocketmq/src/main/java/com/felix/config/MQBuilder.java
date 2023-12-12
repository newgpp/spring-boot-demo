package com.felix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Objects;

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

    public ProducerConsumerBuilder getProducer(String name) {
        return producers.stream().filter(x -> Objects.equals(name, x.getName()))
                .findFirst().orElse(null);
    }

    public ProducerConsumerBuilder getConsumer(String name) {
        return consumers.stream().filter(x -> Objects.equals(name, x.getName()))
                .findFirst().orElse(null);
    }


}
