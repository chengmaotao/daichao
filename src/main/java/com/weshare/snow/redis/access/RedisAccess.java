package com.weshare.snow.redis.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisAccess {

    private final static String keyName = "daichao:";// 规范要求，redis的key需加上项目名称
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * @Description: 存入String
     * @param:
     * @date: 2019/3/14 13:16
     */
    public void setString(String key, Object value) {
        redisTemplate.opsForValue().set(keyName + key, value);
    }

    /**
     * @Description: 存入String并设置过期时间
     * @param: key
     * @param: value 值
     * @param: timeOut 过期时间
     * @param: timeUnit 时间单位
     * @date: 2019/3/14 14:03
     */
    public void setStringTime(String key, Object value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(keyName + key, value, timeOut, timeUnit);
    }

    /**
     * @Description: 根据key取String
     * @param:
     * @date: 2019/3/14 13:16
     */
    public Object getString(String key) {
        ValueOperations<Object, Object> string = redisTemplate.opsForValue();
        return string.get(keyName + key);
    }

    /**
     * @Description: 根据key删除缓存
     * @param: key
     * @date: 2019/3/19 20:16
     */
    public void removeString(String key) {
        redisTemplate.delete(keyName + key);
    }

    /**
     * @Description: 存入hash
     * @param: key
     * @param: value 值
     * @date: 2019/3/14 12:56
     */
    public void setHash(String key, HashMap<Object, Object> value) {
        HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(keyName + key, value);
    }

    /**
     * @Description: 存入hash并设置过期时间
     * @param: key
     * @param: value 值
     * @param: timeOut 过期时间
     * @param: timeUnit 时间单位
     * @date: 2019/3/14 13:51
     */
    public void setHashTime(String key, HashMap<Object, Object> value, long timeOut, TimeUnit timeUnit) {
        HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(keyName + key, value);
        redisTemplate.expire(keyName + key, timeOut, timeUnit);
    }

    /**
     * @Description: 根据key获取Hash
     * @param: key
     * @return: Map<Object , Object>
     * @date: 2019/3/14 11:52
     */
    public Map<Object, Object> getHash(String key) {
        HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(keyName + key);
    }

    public long redisIncrement(String key, int num) {
        return redisTemplate.opsForValue().increment(key, num);
    }

    public void setExpireTime(String key, long timeOut, TimeUnit timeUnit) {
        redisTemplate.expire(keyName + key, timeOut, timeUnit);
    }
}
