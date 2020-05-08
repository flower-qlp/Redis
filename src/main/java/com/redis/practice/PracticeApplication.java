package com.redis.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author itoutsource.cz10
 */
@SpringBootApplication(scanBasePackages = {"com.redis.practice"})
public class PracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}
