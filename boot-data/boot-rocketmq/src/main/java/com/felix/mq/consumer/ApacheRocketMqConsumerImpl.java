package com.felix.mq.consumer;

import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author felix
 */
public class ApacheRocketMqConsumerImpl implements MqConsumer {

    private static final Logger log = LoggerFactory.getLogger(ApacheRocketMqConsumerImpl.class);

    private String name;

    private MQPushConsumer mqPushConsumer;

    public ApacheRocketMqConsumerImpl(String name, MQPushConsumer mqPushConsumer) {
        this.name = name;
        this.mqPushConsumer = mqPushConsumer;
    }

    @Override
    public void subscribe(String topic, String tags, ConsumerHandler consumerHandler) throws Exception {
        if (tags != null && !tags.trim().isEmpty()) {
            tags = String.join(" || ", tags.split(","));
        }
        mqPushConsumer.subscribe(topic, tags);
        log.info("====>MQ订阅消息 topic={}, tags={}", topic, tags);
        mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String topic = msg.getTopic();
                    String tags = msg.getTags();
                    String keys = msg.getKeys();
                    byte[] msgBody = msg.getBody();
                    String value = new String(msgBody);
                    log.debug("====>MQ接收消息, topic={}, tag={}, key={}, value={}", topic, tags, keys, value);
                    try {
                        if (!consumerHandler.onConsume(tags, keys, value)) {
                            String msgId = msg.getMsgId();
                            int reconsumeTimes = msg.getReconsumeTimes();
                            log.error("====>MQ消费失败-重新消费, topic={}, msgId={}, reconsumeTimes={}, tag={}, key={}, value={}", msgId, reconsumeTimes, topic, tags, keys, value);
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        }
                    } catch (Exception e) {
                        log.error("====>MQ消费异常, topic={}, msgId={}, reconsumeTimes={}, tag={}, key={}, value={}", msg.getMsgId(), msg.getReconsumeTimes(), topic, tags, keys, value);
                        log.error("====>MQ消费异常: ", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        mqPushConsumer.start();
    }

    @Override
    public void subscribe(String topic, ConsumerHandler consumerHandler) throws Exception {
        this.subscribe(topic, null, consumerHandler);
    }

    @Override
    @PreDestroy
    public void shutdown() {
        mqPushConsumer.shutdown();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MQPushConsumer getMqPushConsumer() {
        return mqPushConsumer;
    }

    public void setMqPushConsumer(MQPushConsumer mqPushConsumer) {
        this.mqPushConsumer = mqPushConsumer;
    }
}
