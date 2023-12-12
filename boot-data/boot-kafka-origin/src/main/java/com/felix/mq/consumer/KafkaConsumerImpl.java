package com.felix.mq.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * @author felix
 */
public class KafkaConsumerImpl implements MqConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private String name;

    private KafkaConsumer<String, String> kafkaConsumer;

    public KafkaConsumerImpl(String name, KafkaConsumer<String, String> kafkaConsumer) {
        this.name = name;
        this.kafkaConsumer = kafkaConsumer;
    }

    @Override
    public void subscribe(String topic, String tags, ConsumerListener consumerListener) throws Exception {
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(5L));
        for (ConsumerRecord<String, String> record : records) {
            int partition = record.partition();
            long offset = record.offset();
            String key = record.key();
            String value = record.value();
            log.debug("====>MQ接收消息, topic={}, key={}, value={}", topic, key, value);
            if (!consumerListener.onConsume(tags, key, value)) {
                log.error("====>MQ消费失败, topic={}, partition={}, offset={}, key={}, value={}", topic, partition, offset, key, value);
            }
        }
        kafkaConsumer.commitAsync();
    }

    @Override
    public void subscribe(String topic, ConsumerListener consumerListener) throws Exception {
        this.subscribe(topic, null, consumerListener);
    }

    @Override
    public void shutdown() {
        kafkaConsumer.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void setKafkaConsumer(KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }
}
