package com.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.wenda.dao.RedisDao;
import com.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by xrh
 * 11:21 AM on 12/9/19 2019
 * 将代办事件加入EventQueue队列
 */
@Service
public class EventProducer {

    @Autowired
    RedisDao redisDao;

    public boolean fireEvent(EventModel eventModel){
        try {
            String event = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            redisDao.lpush(key,event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
