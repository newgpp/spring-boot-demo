package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author felix
 * @desc some desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaLogAppenderTest {

    private static final Logger log = LoggerFactory.getLogger(KafkaLogAppenderTest.class);

    @Test
    public void append_log_info_should_success() {

        //info
        ObjectNode put = new ObjectMapper().createObjectNode().put("log", "hello").put("ts", System.currentTimeMillis());
        log.info("append_log_info_should_success, message: {}", put.toString());

        //error
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            log.error("计算异常 error: ", e);
        }
    }
}
