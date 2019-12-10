package com.wenda.async;

import com.alibaba.fastjson.JSON;
import com.wenda.dao.RedisDao;
import com.wenda.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by xrh
 * 1:28 PM on 12/9/19 2019
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    //将Event类型与处理该类型的EventHandler一一对应起来存储到 config 中
    private Map<EventType,List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    RedisDao redisDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        //找出所有implements EventHandler 的 bean
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String,EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for(EventType type : eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<>());
                    }
                    //config.get(type) 得到此type对应的 list<EventHandler>
                    config.get(type).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = redisDao.brpop(0, key);

                    //redis返回的第一个值是队列对应的key，将其过滤掉
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别此事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
