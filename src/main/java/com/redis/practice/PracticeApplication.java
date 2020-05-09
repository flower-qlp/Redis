package com.redis.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author itoutsource.cz10
 */
@SpringBootApplication(scanBasePackages = {"com.redis.practice"})
@EnableFeignClients(basePackages = {"com.redis.practice.feignclient"})
public class PracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}
