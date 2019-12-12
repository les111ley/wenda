package com.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.wenda.async.EventHandler;
import com.wenda.async.EventModel;
import com.wenda.async.EventType;
import com.wenda.dao.RedisDao;
import com.wenda.model.EntityType;
import com.wenda.model.Feed;
import com.wenda.model.Question;
import com.wenda.model.User;
import com.wenda.service.*;
import com.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Create by xrh
 * 4:30 PM on 12/11/19 2019
 */
@Component
public class feedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    RedisDao redisDao;

    //生成feed的内容（data）
    private String buildFeedData(EventModel eventModel){
        Map<String,String> map = new HashMap<>();
        User actor = userService.getUser(eventModel.getActorId());
        if(actor != null){
            return null;
        }
        //获取发出新鲜事的用户的个人信息
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        //当评论一个问题或者关注一个问题，都产生一条feed
        if(eventModel.getType() == EventType.COMMENT ||
                (eventModel.getType() == EventType.FOLLOW && eventModel.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.getById(eventModel.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel eventModel) {
        //为了测试，把model的userId随机一下
        Random r = new Random();
        eventModel.setActorId(1+r.nextInt(10));

        //构造一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(eventModel.getType().getValue());
        feed.setUserId(eventModel.getActorId());
        feed.setData(buildFeedData(eventModel));
        if (feed.getData() == null) {
            // 不支持的feed(非评论问题 && 非关注问题，或者被非法调用，不存在Actor)
            return;
        }
        //将生成的feed存入数据库
        feedService.addFeed(feed);

        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, eventModel.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            redisDao.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW,EventType.COMMENT});
    }
}
