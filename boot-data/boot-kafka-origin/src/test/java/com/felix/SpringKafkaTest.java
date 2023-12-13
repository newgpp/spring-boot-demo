package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.felix.mq.producer.MqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

/**
 * @author felix
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaTest {

    private static final Logger log = LoggerFactory.getLogger(SpringKafkaTest.class);

    @Autowired
    private MqProducer orderPlaceProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void send_message_sync_should_success() {
        ObjectNode o1 = objectMapper.createObjectNode();
        o1.put("type", "sync");
        o1.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
        o1.put("ts", System.currentTimeMillis());
        try {
            boolean place = orderPlaceProducer.send("place", null, o1.toString());
            log.info("send message sync result={}", place);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void send_message_async_should_success() {

        Stream.iterate(0, x -> x + 1).limit(50).forEach(x -> {
            ObjectNode o2 = objectMapper.createObjectNode();
            o2.put("type", "async");
            o2.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
            o2.put("ts", System.currentTimeMillis());
            try {
                orderPlaceProducer.sendAsync("pay", null, o2.toString(), new MqProducer.Callback() {
                    @Override
                    public void onSuccess() {
                        log.info("send message async success");
                    }

                    @Override
                    public void onFail(Throwable e, String topic, String tag, String key, String body) {
                        log.error("send message fail topic={}, tag={}, key={}, body={}", topic, tag, key, body);
                        if (e != null) {
                            log.error("send message error: ", e);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consume_message_should_success() {
        System.out.println("------------------");
        System.out.println("111111111111111111");
    }
}
