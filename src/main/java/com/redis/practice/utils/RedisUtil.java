package com.redis.practice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author happy
 */
@Component
public class RedisUtil {

    private final  long DEFAULT_EXPIRE_TIME=24*60*60;

    private final long NOT_EXPIRE=-1;

    @Autowired
    public static RedisTemplate<String, Object> redisTemplate;

    public void deleteKeys(String... keys) {
        Set<String> kSet = Stream.of(keys).map(x -> x).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    public void expireKey(String key, long time, TimeUnit timeUnit){
         redisTemplate.expire(key,time,timeUnit);
    }
}
