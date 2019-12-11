package com.wenda.service;

import com.wenda.dao.RedisDao;
import com.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Create by xrh
 * 9:34 AM on 12/11/19 2019
 */
@Service
public class FollowService {
    @Autowired
    RedisDao redisDao;

    //加关注
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = redisDao.getJedis();
        Transaction tx = redisDao.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = redisDao.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;

    }

    //取消关注
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = redisDao.getJedis();
        Transaction tx = redisDao.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = redisDao.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    //将zset获取到的数据（以set结构存储）储存到一个列表中
    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //获取某实体的关注者列表（某实体的粉丝）
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisDao.zrange(followerKey, 0, count));
    }

    //翻页
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisDao.zrange(followerKey, offset, count));
    }

    //获取当前用户对某类实体的关注列表
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(redisDao.zrevrange(followeeKey, 0, count));
    }

    //翻页
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(redisDao.zrevrange(followeeKey, offset, count));
    }

    //某实体的关注总数
    public long getFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisDao.zcard(followerKey);
    }

    //当前用户对某类实体的关注总数
    public long getFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisDao.zcard(followeeKey);
    }

    //某实体的关注者中有没有当前用户
    public boolean isFollower(int userId,int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisDao.zscore(followerKey,String.valueOf(userId)) != null;
    }
}
