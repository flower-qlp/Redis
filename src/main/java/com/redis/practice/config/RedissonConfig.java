package com.redis.practice.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author happy
 * redisson config
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.url}")
    private String url;

    @Value("${spring.redis.password}")
    private String password;

    @Profile("dev")
    @Bean(name = "RedissonClient")
    public RedissonClient redissonClientSingle() {
        RedissonClient redissonClient = null;
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + url).setPassword(password);
        redissonClient = Redisson.create(config);
        return redissonClient;
    }

    @Profile("test")
    @Bean(name = "RedissonClient")
    public RedissonClient redissonClientCluster() {
        String[] nodes = url.split(",");
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = "redis://" + nodes[i];
        }
        RedissonClient redissonClient = null;
        Config config = new Config();
        config.useClusterServers().setScanInterval(2000)
                .addNodeAddress(nodes)
                .setPassword(password);
        redissonClient = Redisson.create(config);
        return redissonClient;
    }


}
