package com.wenda.async;

import java.util.List;

/**
 * Create by xrh
 * 1:24 PM on 12/9/19 2019
 * doHandle 处理事件
 * getSupportEventTypes 支持处理哪一类事件
 */
public interface EventHandler {

    void doHandle(EventModel eventModel);

    List<EventType> getSupportEventTypes();
}
