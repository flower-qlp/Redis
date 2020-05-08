package com.redis.practice.sales;

import com.redis.practice.service.RedisService;
import com.redis.practice.utils.RedissonLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTest {

    private final String lockKey = "productNum:redisson";

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Autowired
    private RedisService redisService;

    @Test
    public void updateProductNumRedisson() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new MyTask("thread" + i)).start();
        }
        Thread.sleep(10000);
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
