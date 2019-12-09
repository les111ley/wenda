package com.wenda.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Create by xrh
 * 3:34 PM on 12/8/19 2019
 * redis 数据库接口层
 * 对redis数据库中的数据进行增删查改
 */
@Repository
public class RedisDao implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedisDao.class);
    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    //向集合set中添加元素
    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch(Exception e){
            logger.error("添加元素异常" + e.getMessage());
        }finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //删除集合set中元素
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("移除元素异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //集合set中元素总数
    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("统计元素个数异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //判断集合set中是否有某元素
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        } catch (Exception e) {
            logger.error("判断元素是否存在异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }
}
