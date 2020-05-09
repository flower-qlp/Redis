package com.redis.practice.service;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author happy
 */
@Service
public class RedisService {

    @Resource
    private ValueOperations<String, Object> valueOperations;

    public void updateProductNum(String key, String user) {
        Integer productNum = 0;
        Object o = valueOperations.get(key);
        if (o != null) {
            productNum = Integer.valueOf(String.valueOf(o));
        }
        if (productNum >= 1) {
            System.out.println(user + "得到第" + productNum + "个商品！");
            productNum -= 1;
            valueOperations.set(key, productNum);
        }
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    public boolean incr(String key) {
        try {
            valueOperations.increment(key);
            return true;
        } catch (Exception e) {
        }
        return false;
    }


}
