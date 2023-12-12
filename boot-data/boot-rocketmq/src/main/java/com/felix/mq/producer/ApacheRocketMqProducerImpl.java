package com.felix.mq.producer;

import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author felix
 */
public class ApacheRocketMqProducerImpl implements MqProducer {

    private static final Logger log = LoggerFactory.getLogger(ApacheRocketMqProducerImpl.class);

    private String name;
    private String topic;
    private MQProducer mqProducer;

    public ApacheRocketMqProducerImpl(String name, String topic, MQProducer mqProducer) {
        this.name = name;
        this.topic = topic;
        this.mqProducer = mqProducer;
    }

    @Override
    public void shutdown() {
        mqProducer.shutdown();
    }

    @Override
    public boolean send(String tag, String key, String body) throws Exception {
        Message message = new Message(topic, tag, key, body.getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = mqProducer.send(message);
        return sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    @Override
    public void sendAsync(String tag, String key, String body, Callback callback) {
        Message msg = new Message(topic, tag, key, body.getBytes(StandardCharsets.UTF_8));
        try {
            mqProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(final SendResult sendResult) {
                    if (callback != null) {
                        if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                            callback.onSuccess();
                        } else {
                            String msgId = sendResult.getMsgId();
                            log.error("MQ发送消息失败, msgId={}, topic={}, tag={}, key={}, body={}", msgId, topic, tag, key, body);
                            callback.onFail(null, topic, tag, key, body);
                        }
                    }
                }

                @Override
                public void onException(final Throwable e) {
                    log.error("MQ发送消息异常, topic={}, tag={}, key={}, body={}", topic, tag, key, body);
                    log.error("MQ发送消息异常: ", e);
                    callback.onFail(e, topic, tag, key, body);
                }
            });
        } catch (Exception e) {
            log.error("MQ发送消息失败, topic={}, tag={}, key={}, body={}", topic, tag, key, body);
            log.error("MQ发送消息失败: ", e);
        }
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

    public MQProducer getMqProducer() {
        return mqProducer;
    }

    public void setMqProducer(MQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }
}
