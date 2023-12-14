package com.felix.mq.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author felix
 */
public class KafkaConsumerImpl implements MqConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private static volatile boolean running = true;

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private String name;

    private KafkaConsumer<String, String> kafkaConsumer;

    public KafkaConsumerImpl(String name, KafkaConsumer<String, String> kafkaConsumer) {
        this.name = name;
        this.kafkaConsumer = kafkaConsumer;
    }

    @Override
    public void subscribe(String topic, String tags, ConsumerHandler consumerHandler) throws Exception {
        //订阅topic
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        Runnable runnable = () -> {
            while (running) {
                try {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(5L));
                    log.debug("====>MQ拉取消息数量, records.size()={}", records.count());
                    for (TopicPartition topicPartition : records.partitions()) {
                        List<ConsumerRecord<String, String>> partitionRecords = records.records(topicPartition);
                        for (ConsumerRecord<String, String> record : partitionRecords) {
                            int partition = record.partition();
                            long offset = record.offset();
                            String key = record.key();
                            String value = record.value();
                            log.debug("====>MQ接收消息, topic={}, partition={}, offset={}, key={}, value={}", topic, partition, offset, key, value);
                            try {
                                if (!consumerHandler.onConsume(tags, key, value)) {
                                    log.error("====>MQ消费失败, topic={}, partition={}, offset={}, key={}, value={}", topic, partition, offset, key, value);
                                }
                            } catch (Exception e) {
                                log.error("====>MQ消费异常: ", e);
                            }
                        }
                        //按partition提交偏移量
                        long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                        long commitOffset = lastOffset + 1;
                        try {
                            kafkaConsumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(commitOffset)));
                            log.debug("====>MQ提交偏移量成功, topicPartition={}, lastOffset={}, commitOffset={}", topicPartition, lastOffset, commitOffset);
                        } catch (Exception e) {
                            log.error("====>MQ提交偏移量失败, topicPartition={}, lastOffset={}, commitOffset={}", topicPartition, lastOffset, commitOffset);
                            log.error("====>MQ提交偏移量异常: ", e);
                        }
                    }
                } catch (Exception e) {
                    log.error("====>MQ拉取消息异常: ", e);
                }
            }
            kafkaConsumer.close();
            log.info("====>MQ消费者已关闭...");
            countDownLatch.countDown();
        };
        new Thread(runnable).start();
    }

    @Override
    public void subscribe(String topic, ConsumerHandler consumerHandler) throws Exception {
        this.subscribe(topic, null, consumerHandler);
    }

    @Override
    @PreDestroy
    public void shutdown() {
        running = false;
        log.info("====>MQ消费者关闭中...");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
