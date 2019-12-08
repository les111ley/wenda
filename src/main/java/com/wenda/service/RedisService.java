package com.wenda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Create by xrh
 * 3:34 PM on 12/8/19 2019
 */
@Service
public class RedisService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    //向集合中添加元素
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

    //删除集合中元素
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

    //集合中元素总数
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

    //判断集合中是否有某元素
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
