package com.felix.dao;

import com.felix.entity.AlarmEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlarmEventMapper {

    int deleteByPrimaryKey(Long alarmEventId);

    int insert(AlarmEvent record);

    int insertSelective(AlarmEvent record);

    AlarmEvent selectByPrimaryKey(Long alarmEventId);

    int updateByPrimaryKeySelective(AlarmEvent record);

    int updateByPrimaryKeyWithBLOBs(AlarmEvent record);

    int updateByPrimaryKey(AlarmEvent record);

    int deleteByAlarmEventType(Integer alarmEventType);

    List<AlarmEvent> selectLatestEvents(@Param("alarmEventTypes") List<Integer> alarmEventTypes, @Param("limit") Integer limit);

    /**
     * 确定是否为同一个事件
     */
    AlarmEvent selectByCodeReportTs(@Param("alarmEventCodeOrigin") String alarmEventCodeOrigin, @Param("reportTs") Long reportTs);

    /**
     * 获取未完成的事件
     */
    List<AlarmEvent> selectUnfinishedEvents(@Param("alarmEventType") Integer alarmEventType, @Param("limit") Integer limit);

    /**
     * 更新报警事件的结束时间
     */
    int updateFinishedEvent(@Param("alarmEventCodeOrigin") String eventCode, @Param("finishTs") long finishTs);

    /**
     * 查询列表
     */
    List<AlarmEvent> selectList(Map<String, Object> map);

    AlarmEvent getLatestEvent(@Param("alarmEventType") Integer alarmEventType, @Param("berthId") Long berthId, @Param("limit") Integer limit);
}