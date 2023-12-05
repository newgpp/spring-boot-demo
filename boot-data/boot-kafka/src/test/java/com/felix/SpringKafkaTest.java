package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author felix
 * @desc some desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void send_message_should_success() throws Exception {
        ObjectNode objectNode = objectMapper.createObjectNode();
        long ts = System.currentTimeMillis();
        objectNode.put("ts", ts);
        kafkaTemplate.send("my-topic", String.valueOf(ts), objectNode.toString());
        kafkaTemplate.flush();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

}
