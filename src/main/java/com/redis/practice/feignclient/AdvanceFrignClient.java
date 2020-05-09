package com.redis.practice.feignclient;

import com.redis.practice.bean.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "advance",url = "127.0.0.1:8081")
public interface AdvanceFrignClient {

    @GetMapping(value = "/user-code/findAll")
    ResponseDto findAllUserCode();

    @GetMapping(value = "/user/findAll")
    ResponseDto findAllUser();
}
