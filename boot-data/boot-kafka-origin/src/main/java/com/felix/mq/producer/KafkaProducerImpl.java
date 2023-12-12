package com.felix.mq.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * @author felix
 */
public class KafkaProducerImpl implements MqProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerImpl.class);

    private String name;
    private String topic;
    private KafkaProducer<String, String> kafkaProducer;

    public KafkaProducerImpl(String name, String topic, KafkaProducer<String, String> kafkaProducer) {
        this.name = name;
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void shutdown() {
        kafkaProducer.close();
    }

    @Override
    public boolean send(String key, String body) {
        return this.send(null, key, body);
    }

    @Override
    public boolean send(String tag, String key, String body) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, body);
        try {
            Future<RecordMetadata> future = kafkaProducer.send(record);
            RecordMetadata recordMetadata = future.get();
            return true;
        } catch (Exception e) {
            log.error("MQ发送消息失败, topic={}, tag={}, key={}, body={}", topic, tag, key, body);
            log.error("MQ发送消息异常: ", e);
            return false;
        }
    }

    @Override
    public void sendAsync(String tag, String key, String body, Callback callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, body);
        try {
            kafkaProducer.send(record, (metadata, e) -> {
                if (e != null) {
                    callback.onFail(e, topic, tag, key, body);
                    log.error("MQ发送消息失败, topic={}, tag={}, key={}, body={}", topic, tag, key, body);
                    log.error("MQ发送消息异常: ", e);
                } else {
                    callback.onSuccess();
                }
            });
        } catch (Exception e) {
            callback.onFail(e, topic, tag, key, body);
        }
    }

    @Override
    public void sendAsync(String key, String body, Callback callback) {
        this.sendAsync(null, key, body, callback);
    }

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


    public KafkaProducer<String, String> getKafkaProducer() {
        return kafkaProducer;
    }

    public void setKafkaProducer(KafkaProducer<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
}
