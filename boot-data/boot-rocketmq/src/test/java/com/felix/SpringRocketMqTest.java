package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.felix.mq.producer.MqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author felix
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRocketMqTest {

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
            System.out.println(place);
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
        ObjectNode o2 = objectMapper.createObjectNode();
        o2.put("type", "async");
        o2.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
        o2.put("ts", System.currentTimeMillis());
        try {
            orderPlaceProducer.sendAsync("pay", null, o2.toString(), new MqProducer.Callback() {
                @Override
                public void onStatus(boolean succeed) {
                    System.out.println("status: " + succeed);
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
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
    public void consume_message_should_success() {

        System.out.println("------------------");
        System.out.println("111111111111111111");


    }
}
