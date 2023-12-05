package com.felix.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author felix
 * @desc some desc
 */
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "my-topic", groupId = "my-group-1", containerFactory = "kafkaListenerContainerFactory")
    public void greetingListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            // process greeting message
            System.out.println("------------" + records.size() + "-------------");
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key());
                System.out.println(record.value());
            }
            //手动提交偏移量
            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
