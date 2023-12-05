package com.felix.config;

import com.felix.mq.listener.LoginStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisStreamConfig.class);

    @Value("${redis.stream.login.key:login-events}")
    private String streamLoginKey;
    @Value("${redis.stream.login.group:login-group-1}")
    private String streamLoginGroup;

    @Value("${eureka.instance.instance-id:111111}")
    private String instanceId;

    /**
     * 创建消费者组
     */
    private void createConsumerGroupIfNotExists(RedisConnectionFactory factory,
                                                String streamKey, String groupName) {
        try {
            factory.getConnection().streamCommands()
                    .xGroupCreate(streamKey.getBytes(), groupName, ReadOffset.from("0-0"), true);
        } catch (RedisSystemException exception) {
            log.error("xgroup create error: {}", exception.getCause().getMessage());
        }
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(factory, options);
        container.start();
        return container;
    }


    @Bean
    public StreamListener<String, MapRecord<String, String, String>> loginStreamListener() {
        return new LoginStreamListener();
    }

    /**
     * 登录消息订阅
     */
    @Bean
    public Subscription subscription(RedisConnectionFactory factory, StreamMessageListenerContainer<String, MapRecord<String, String, String>> container) {
        createConsumerGroupIfNotExists(factory, streamLoginKey, streamLoginGroup);
        Subscription subscription = container.receive(
                Consumer.from(streamLoginGroup, instanceId),
                StreamOffset.create(streamLoginKey, ReadOffset.lastConsumed()),
                loginStreamListener()
        );
        return subscription;
    }


}
