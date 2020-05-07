package com.redis.practice.controller;

import com.redis.practice.bean.dto.JedisDto;
import com.redis.practice.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author happy
 */
@RestController
public class RedisController {

    @Autowired
    private JedisService jedisService;

    @GetMapping(value = "/get/{key}")
    public String getByKey(
            @PathVariable("key") String key
    ) {
        return jedisService.get(key, String.class);
    }

    @PostMapping(value = "/set")
    public boolean setValue(
            @RequestBody JedisDto dto
    ) {
        return jedisService.set(dto.getKey(), dto.getValue());
    }

}
