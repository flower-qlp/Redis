package com.redis.practice.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author happy
 * 通过Jedis操作redis
 */
@Service
public class JedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取对象
     **/
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            return stringToBean(str, clazz);
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 设置对象
     **/
    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (StringUtils.isEmpty(key)) {
                return false;
            }
            jedis.set(key, beanToString(value));
            return true;
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 删除
     * **/
    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long ret = jedis.del(key);
            return ret > 0;
        } finally {
            closeJedis(jedis);
        }
    }


    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public <T> T stringToBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public <T> String beanToString(T value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        Class clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else {
            return JSON.toJSONString(value);
        }
    }
}
