package com.felix.entity.dto;

/**
 * @author felix
 */
public class LoginEvent {

    private String eventId;

    private String name;

    private Integer age;

    private Long ts;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", ts=" + ts +
                '}';
    }
}
