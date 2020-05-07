package com.redis.practice.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisDto {
    private String host;
    private int port;
    private String passWord;
    private int timeout;
    private int db;
    private int poolMaxTotal;
    private int poolMaxWait;
    private int poolMaxIdle;
}
