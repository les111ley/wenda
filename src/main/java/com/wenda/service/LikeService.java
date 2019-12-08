package com.wenda.service;

import com.wenda.service.RedisService;
import com.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by xrh
 * 3:52 PM on 12/8/19 2019
 */
@Service
public class LikeService {
    @Autowired
    RedisService redisService;

    //点赞
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        redisService.sadd(likeKey, String.valueOf(userId));

        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        redisService.srem(dislikeKey, String.valueOf(userId));

        return redisService.scard(likeKey);
    }

    //点踩
    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        redisService.sadd(dislikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        redisService.srem(likeKey, String.valueOf(userId));

        return redisService.scard(likeKey);
    }

    //用户对某个entity的点赞/踩的状态
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if (redisService.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        } else if (redisService.sismember(dislikeKey, String.valueOf(userId))) {
            return -1;
        } else {
            return 0;
        }
    }

    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return redisService.scard(likeKey);
    }
}
