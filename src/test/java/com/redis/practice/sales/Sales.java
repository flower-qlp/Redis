package com.redis.practice.sales;

import com.redis.practice.service.JedisService;
import com.redis.practice.service.RedisService;
import com.redis.practice.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 多线程测试redis
 * junit测试线程  需要让主线程等子线程结束之后再结束
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Sales {

    @Autowired
    private RedisService redisService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 简单的判断库存 引起的超卖
     **/
    @Test
    public void updateProductNum() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new MyThread("thread" + i)).start();
        }
        Thread.sleep(10000);
    }

    /**
     * 添加关键字 --当前可以防止超卖
     * 会发生分布式问题
     * 导致多线程排队
     **/
    @Test
    public void updateProductNumSynchronized() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new MyThreadSSynchronized("thread" + i, "productNum")).start();
        }
        Thread.sleep(10000);
    }

    /**
     * 分布式锁
     */
    @Test
    public void updateProductNumSetNx() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new MyThreadSetNx("thread" + i)).start();
        }
        Thread.sleep(10000);
    }

    @Test
    public void updateProductNumJedisPool() throws InterruptedException {
        new Thread(() -> {
            Integer productNum = jedisService.get("productNum", Integer.class);
            System.out.println("productNum-------->" + productNum);
        }).start();
        Thread.sleep(5000);
    }

    public class MyThread implements Runnable {

        private String threadName;

        public MyThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            Integer time = ((int) (Math.random() * 1000)) % 4000;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisService.updateProductNum("productNum", threadName);
        }
    }

    public class MyThreadSSynchronized implements Runnable {

        private String threadName;
        private String key;

        public MyThreadSSynchronized(String threadName, String key) {
            this.threadName = threadName;
            this.key = key;
        }

        @Override
        public void run() {
            Integer time = ((int) (Math.random() * 1000)) % 4000;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (key) {
                redisService.updateProductNum(key, threadName);
            }
        }
    }

    /**
     * 分布式锁 --解决分布式问题
     * 1.需要对不同的锁给定唯一的 value 防止线程间误删
     * 2.需要定时任务 对流程监听 ，未完成 是定延长时间
     **/
    public class MyThreadSetNx implements Runnable {

        private String threadName;

        private final String lockKey = "productNum:1:lock";

        public MyThreadSetNx(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            Integer time = ((int) (Math.random() * 1000)) % 4000;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean flag = redisUtil.setNx(lockKey, lockKey);
            if (!flag) {
                System.out.println("服务器正忙" + threadName + "稍等");
                return;
            }
            redisUtil.expireKey(lockKey, 10, TimeUnit.SECONDS);
            try {
                redisService.updateProductNum("productNum", threadName);
            } finally {
                redisUtil.deleteKeys(lockKey);
            }
        }
    }


}
