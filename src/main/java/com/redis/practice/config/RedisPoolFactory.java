package com.redis.practice.config;

import com.redis.practice.bean.RedisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author happy
 * Jedis
 * 创建redis pool
 */
@Component
public class RedisPoolFactory {

     @Autowired
     private RedisDto redisDto;

     @Bean
    public JedisPool getJedisPool(){
         JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
         jedisPoolConfig.setMaxIdle(redisDto.getPoolMaxIdle());
         jedisPoolConfig.setMaxTotal(redisDto.getPoolMaxTotal());
         jedisPoolConfig.setMaxWaitMillis(redisDto.getPoolMaxWait());
         JedisPool jp=new JedisPool(jedisPoolConfig,redisDto.getHost(),
                 redisDto.getPort(),redisDto.getTimeout(),redisDto.getPassWord(),redisDto.getDb());
         return jp;
     }

}
