package com.felix.mq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felix.entity.constants.RedisConstant;
import com.felix.service.RedisLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoginStreamListener implements StreamListener<String, MapRecord<String, String, String>>, CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoginStreamListener.class);

    @Value("${redis.stream.login.key:login-events}")
    private String streamLoginKey;
    @Value("${redis.stream.login.group:login-group-1}")
    private String streamLoginGroup;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisLockService redisLockService;

    @Override
    public void onMessage(MapRecord<String, String, String> record) {
        log.info("stream listener received record, key: {}, group: {}, record: {}", streamLoginKey
                , streamLoginGroup, record);
        Map<String, String> message = record.getValue();
        try {
            String value = objectMapper.writeValueAsString(message);
            log.info("onMessage: {}", value);
        } catch (Exception e) {
            log.error("consume message error: ", e);
        }
        RecordId recordId = record.getId();
        redisTemplate.opsForStream().acknowledge(streamLoginKey, streamLoginGroup, recordId);
        log.info("stream listener ack record, key: {}, group: {}, recordId: {}", streamLoginKey
                , streamLoginGroup, recordId);
    }

    /**
     * 未ack消息补偿
     */
    @Override
    public void run(String... args) throws Exception {
//        while (true) {
//            TimeUnit.SECONDS.sleep(30);
//            String uuid = UUID.randomUUID().toString();
//            try {
//                if (redisLockService.lock(RedisConstant.STREAM_LOGIN_PENDING_LOCK, uuid, 30)) {
//                    supplyPendingMessages();
//                }
//            } finally {
//                redisLockService.unlock(RedisConstant.STREAM_LOGIN_PENDING_LOCK, uuid);
//            }
//        }
    }

    private void supplyPendingMessages() {
        //获取pending消息
        List<PendingMessage> pendingMessages = redisTemplate.opsForStream()
                .pending(streamLoginKey, streamLoginGroup, Range.unbounded(), Long.MAX_VALUE)
                .stream().collect(Collectors.toList());
        Map<RecordId, PendingMessage> pendingMessageMap = pendingMessages.stream()
                //当未确认消息时间超过2分钟才重新投递消息，防止正在处理的消息被重新投递
                .filter(e -> e.getElapsedTimeSinceLastDelivery().getSeconds() > 120)
                .collect(Collectors.toMap(PendingMessage::getId, Function.identity()));

        Set<RecordId> recordIds = pendingMessageMap.keySet();
        if (recordIds.isEmpty()) {
            return;
        }
        List<String> sortedMessageIds = recordIds.stream().map(RecordId::getValue)
                .sorted(Comparator.comparingLong(messageId -> Long.parseLong(messageId.split("-")[0])))
                .sorted(Comparator.comparingInt(messageId -> Integer.parseInt(messageId.split("-")[1])))
                .collect(Collectors.toList());
        log.info("未确认超过2分钟消息获取, key {}, groupName {}, Id {}", streamLoginKey, streamLoginGroup, sortedMessageIds);
        //消息范围 闭区间
        Range<String> range = Range.closed(sortedMessageIds.get(0), sortedMessageIds.get(sortedMessageIds.size() - 1));
        List<MapRecord> pendingRecords = redisTemplate.opsForStream()
                .range(streamLoginKey, range).stream()
                //只取pending消息
                .filter(e -> pendingMessageMap.containsKey(e.getId()))
                .collect(Collectors.toList());
        for (MapRecord pendingRecord : pendingRecords) {
            log.info("未确认超过2分钟, key {}, groupName {}, record {}", streamLoginKey, streamLoginGroup, pendingRecord);
            this.onMessage(pendingRecord);
        }
    }
}
