package com.redis.practice.interfa;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author happy
 */
public interface AbstractDistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waiTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

}
