package com.wenda.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by xrh
 * 11:01 AM on 12/9/19 2019
 */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;

    private Map<String, String> exts = new HashMap<String, String>();

    public EventModel(EventType type) {
        this.type = type;
    }
    public EventModel() {
    }

    public String getExts(String key) {
        return exts.get(key);
    }

    //return this 对象方便链式调用setXXX
    public EventModel setExts(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

}
