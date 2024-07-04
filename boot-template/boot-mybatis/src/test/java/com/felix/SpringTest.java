package com.felix;

import com.felix.controller.AlarmEventController;
import com.felix.entity.AlarmEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTest {

    @Autowired
    private AlarmEventController alarmEventController;

    @Test
    public void get_list_should_success() {

        List<AlarmEvent> latest = alarmEventController.getLatest(5);

        System.out.println(latest);
    }
}
