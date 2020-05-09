package com.redis.practice.sales;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.redis.practice.feignclient.AdvanceFrignClient;
import com.redis.practice.service.RedisService;
import com.redis.practice.utils.RedissonLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTest {

    private final String lockKey = "productNum:redisson";

    private final String bloomFilerKey = "BloomFilter:userCode";

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AdvanceFrignClient advanceFrignClient;

    @Test
    public void updateProductNumRedisson() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new MyTask("thread" + i)).start();
        }
        Thread.sleep(10000);
    }


    @Test
    public void BloomFilter() {
        Object o = advanceFrignClient.findAllUserCode().getData();
        List<String> userCodeList = new ArrayList<>();
        if (o != null) {
            userCodeList = JSONObject.parseArray(JSON.toJSONString(o), String.class);
        }
        for (String userCode : userCodeList) {
            if (redissonLockUtil.containBloomFiler(userCode, bloomFilerKey)) {
                System.out.println("当前用户已经存在过滤器中！" + userCode);
            } else {
                redissonLockUtil.addBloomFilter(userCode, bloomFilerKey);
            }
        }
    }

    @Test
    public void testConnectBloom() throws InterruptedException {

        for(int i=0;i<1000;i++){
            new Thread(new bloomFilterTask("A-0000"+i)).start();
        }
        Thread.sleep(10000);

    }

    public class bloomFilterTask implements Runnable {

        private String userCode;

        public bloomFilterTask(String userCode) {
            this.userCode = userCode;
        }

        @Override
        public void run() {
            try {
                Integer sleepTime = (int) (Math.random() * 1000) % 2000;
                Thread.sleep(sleepTime);
                if (redissonLockUtil.containBloomFiler(userCode, bloomFilerKey)) {
                    System.out.println("欢迎光临！！" + userCode);
                } else {
                    System.out.println("你不是当前系统的用户！" + userCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class MyTask implements Runnable {

        private String taskName;

        public MyTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
                Integer sleepTime = (int) (Math.random() * 1000) % 2000;
                Thread.sleep(sleepTime);

                if (redissonLockUtil.tryLock(lockKey, TimeUnit.SECONDS, 5L, 5L)) {
                    redisService.updateProductNum("productNum", taskName);

                    redissonLockUtil.unlock(lockKey);
                }
            } catch (Exception e) {
                e.printStackTrace();
                redissonLockUtil.unlock(lockKey);
            }
        }
    }


}
