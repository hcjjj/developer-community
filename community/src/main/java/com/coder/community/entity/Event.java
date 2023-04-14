package com.coder.community.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {

    // 主题(即事件类型)
    private String topic;
    // 事件触发者
    private int userId;
    // 实体类型
    private int entityType;
    // 实体id
    private int entityId;
    // 实体的作者
    private int entityUserId;
    // 其他额外的数据
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        // 改造一下方法，返回当前对象，方便链式编程
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        // 方便设置值
        this.data.put(key, value);
        return this;
    }

}
