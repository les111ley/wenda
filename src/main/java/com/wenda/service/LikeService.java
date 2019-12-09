package com.wenda.service;

import com.wenda.dao.RedisDao;
import com.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by xrh
 * 3:52 PM on 12/8/19 2019
 * 用redis实现网站的点赞/踩功能，利用redis中set结构存储记录
 * 记录一条点赞记录，只需记录被点赞的实体ID与点赞的用户ID，两者之间的关系可以用Hashmap储存，所以适合用redis
 * 一个实体可对应多个点赞记录，而点赞记录是无顺序的集合，所以适合用set储存
 */
@Service
public class LikeService {
    @Autowired
    RedisDao redisDao;

    //对某实体（评论 or 问题）点赞，返回点赞后的点赞总数
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        redisDao.sadd(likeKey, String.valueOf(userId));

        //若该用户为该实体点过踩，则删除这条点踩记录
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        redisDao.srem(dislikeKey, String.valueOf(userId));

        return redisDao.scard(likeKey);
    }

    //对某实体（评论 or 问题）点踩，返回点踩后的点赞总数
    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        redisDao.sadd(dislikeKey, String.valueOf(userId));

        //若该用户为该实体点过赞，则删除这条点赞记录
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        redisDao.srem(likeKey, String.valueOf(userId));

        return redisDao.scard(likeKey);
    }

    //用户对某个entity的点赞/踩的状态
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if (redisDao.sismember(likeKey, String.valueOf(userId))) {
            return 1;   //若点过赞，返回1
        } else if (redisDao.sismember(dislikeKey, String.valueOf(userId))) {
            return -1;  //若点过踩，返回-1
        } else {
            return 0;   //若数据库中无点赞/点踩记录，返回0
        }
    }

    //返回某个实体的点赞总数
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return redisDao.scard(likeKey);
    }
}
