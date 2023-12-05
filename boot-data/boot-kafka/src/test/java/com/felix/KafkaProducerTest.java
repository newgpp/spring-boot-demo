package com.felix;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author felix
 * @desc some desc
 *
 */
public class KafkaProducerTest {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerTest.class);
    private static final String topic = "my-topic";
    public static final String MY_GROUP_2 = "my-group-3";
    public static final String BROKERS = "192.168.159.111:9092";

    @Test
    public void send_message_should_success() {
        List<String> messages = Stream.iterate(411, x -> x + 1).limit(1).map(Objects::toString).collect(Collectors.toList());
        sendMessage(topic, messages);
    }

    @Test
    public void consume_message_should_success() {
        List<String> messages = consumeMessage(topic, MY_GROUP_2);
        System.out.println(messages);
    }

    private void sendMessage(String topic, List<String> messages) {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        for (String msg : messages) {
            long ts = System.currentTimeMillis();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, String.valueOf(ts), msg);
            producer.send(record, (metadata, e) -> {
                if (e != null) {
                    log.error("send record failed, record={}, e:", record, e);
                } else {
                    log.info("send record topic={}, partition={}, offset={}", metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
        }
        //把缓冲区的消息立即发送
        producer.flush();
    }

    private List<String> consumeMessage(String topic, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //不自动提交偏移量
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5L));
        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            int partition = record.partition();
            long offset = record.offset();
            String key = record.key();
            String value = record.value();
            log.info("consume message partition={}, offset={}, key={}, value={}", partition, offset, key, value);
            messages.add(value);
        }
        //手动同步提交偏移量
        consumer.commitSync();
        //显示关闭consumer
        consumer.close();
        return messages;
    }

}
