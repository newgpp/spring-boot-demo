package com.felix.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Date;

/**
 * generated at 2024-06-13 09:37:50
 */
public class AlarmEvent {
    /**
     * 报警事件id
     */
    private Long alarmEventId;

    /**
     * 报警事件源编码
     */
    private String alarmEventCodeOrigin;

    /**
     * 报警类型 0-设备离线 1-树枝遮挡 2-镜头模糊 3-相机偏移 4-泊位多车 5-非机动车占用
     */
    private Integer alarmEventType;

    /**
     * 子车场或者独立车场Id
     */
    private Long parkId;

    /**
     * 区域Id
     */
    private Long areaId;

    /**
     * 泊位Id
     */
    private Long berthId;

    /**
     * 父车场id,独立车场和parkId一致
     */
    private Long parentParkId;

    /**
     * 经度，百万分之一度
     */
    private Integer longitude;

    /**
     * 纬度，百万分之一度
     */
    private Integer latitude;

    /**
     * 上报时间戳
     */
    private Long reportTs;

    /**
     * 结束时间戳
     */
    private Long finishTs;
    /**
     * 报警结束状态 0-未结束  1-已结束
     */
    private Integer finishState;

    /**
     * 报警信息
     */
    private String alarmInfo;

    /**
     * 0-未处理 1-已处理
     */
    private Integer eventState;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    /**
     * 报警json对象
     */
    private ObjectNode alarmJson;

    public Long getAlarmEventId() {
        return alarmEventId;
    }

    public void setAlarmEventId(Long alarmEventId) {
        this.alarmEventId = alarmEventId;
    }

    public String getAlarmEventCodeOrigin() {
        return alarmEventCodeOrigin;
    }

    public void setAlarmEventCodeOrigin(String alarmEventCodeOrigin) {
        this.alarmEventCodeOrigin = alarmEventCodeOrigin;
    }

    public Integer getAlarmEventType() {
        return alarmEventType;
    }

    public void setAlarmEventType(Integer alarmEventType) {
        this.alarmEventType = alarmEventType;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public Long getParentParkId() {
        return parentParkId;
    }

    public void setParentParkId(Long parentParkId) {
        this.parentParkId = parentParkId;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Long getReportTs() {
        return reportTs;
    }

    public void setReportTs(Long reportTs) {
        this.reportTs = reportTs;
    }

    public Long getFinishTs() {
        return finishTs;
    }

    public void setFinishTs(Long finishTs) {
        this.finishTs = finishTs;
    }

    public Integer getFinishState() {
        return finishState;
    }

    public void setFinishState(Integer finishState) {
        this.finishState = finishState;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public Integer getEventState() {
        return eventState;
    }

    public void setEventState(Integer eventState) {
        this.eventState = eventState;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public ObjectNode getAlarmJson() {
        return alarmJson;
    }

    public void setAlarmJson(ObjectNode alarmJson) {
        this.alarmJson = alarmJson;
    }
}