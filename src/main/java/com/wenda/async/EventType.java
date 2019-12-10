package com.wenda.async;

/**
 * Create by xrh
 * 10:54 AM on 12/9/19 2019
 * 列举异步事件类型
 */
public enum EventType {
    //public static final EventType LIKE = new EventType(0);
    //public static final EventType COMMENT = new EventType(1);
    //public static final EventType LOGIN = new EventType(2);
    //public static final EventType MESSAGE = new EventType(3);
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MESSAGE(3);

    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
