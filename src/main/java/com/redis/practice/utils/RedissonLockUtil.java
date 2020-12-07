package com.redis.practice.utils;

import com.redis.practice.interfa.AbstractDistributedLocker;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author happy
 * redisson 分布式加锁
 * 分布式布隆过滤器
 */
@Component
public class RedissonLockUtil implements AbstractDistributedLocker {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * tryLock 拿到立刻返回true 拿不到返回false
     * 带时间的tryLock 拿不到等待一段时间 超时返回false
     **/
    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, long waiTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waiTime, leaseTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    public RBloomFilter<Object> getBloomFilter(String filterKey) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(filterKey);
        Long expect = bloomFilter.getExpectedInsertions();
        if (null == expect || expect == 0) {
            //预加载数据量 以及误差率
            bloomFilter.tryInit(5000000L, 0.03);
        }
        return bloomFilter;
    }

    public boolean addBloomFilter(Object o, String filterKey) {
        return this.getBloomFilter(filterKey).add(o);
    }

    public boolean containBloomFiler(Object o, String filterKey) {
        return this.getBloomFilter(filterKey).contains(o);
    }
}
