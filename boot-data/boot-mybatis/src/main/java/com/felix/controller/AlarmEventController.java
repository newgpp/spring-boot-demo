package com.felix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felix.dao.AlarmEventMapper;
import com.felix.entity.AlarmEvent;
import com.felix.vo.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/alarmEvent")
public class AlarmEventController {

    private static final Logger log = LoggerFactory.getLogger(AlarmEventController.class);

    @Resource
    private AlarmEventMapper alarmEventMapper;
    @Resource
    private ObjectMapper objectMapper;

    @GetMapping("/page")
    public PageResult<AlarmEvent> getPage(@RequestParam("alarmEventType") Integer alarmEventType,
                                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("alarmEventType", alarmEventType);
        PageResult<AlarmEvent> pageVO = new PageResult<>();
        try {
            log.info("======>getPage param: {}", objectMapper.writeValueAsString(map));
            Page<AlarmEvent> page = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPage(() -> alarmEventMapper.selectList(map));
            pageVO.setPageNum(pageNum);
            pageVO.setPageSize(pageSize);
            pageVO.setList(page.getResult());
            pageVO.setTotal(page.getTotal());
            return pageVO;
        } catch (Exception e) {
            log.error("======>getPage error: ", e);
            return pageVO;
        }
    }

    @GetMapping("/latest")
    public List<AlarmEvent> getLatest(@RequestParam(name = "limit", defaultValue = "15") Integer limit) {
        try {
            return alarmEventMapper.selectLatestEvents(Arrays.asList(0, 4), limit);
        } catch (Exception e) {
            log.error("======>getLatest error: ", e);
            return Collections.emptyList();
        }
    }

}
