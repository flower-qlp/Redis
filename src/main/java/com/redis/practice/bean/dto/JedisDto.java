package com.redis.practice.bean.dto;

import lombok.Data;

@Data
public class JedisDto {

    private String key;

    private Object value;

}
