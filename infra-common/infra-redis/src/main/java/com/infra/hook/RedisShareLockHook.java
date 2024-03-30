package com.infra.hook;

import com.infra.exception.ShareLockException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisShareLockHook {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RedisShareLockHook.class);


    @Resource
    private RedisHook redisHook;

    private final Long TIME_OUT = 1000L;

    /**
     *  加锁
     */
    public boolean lock(String lockKey, String requestId, Long time) {
        if (Strings.isBlank(lockKey) || Strings.isBlank(requestId) || time <= 0) {
            throw new ShareLockException("分布式锁-加锁参数异常");
        }
        long currentTime = System.currentTimeMillis();
        long outTime = currentTime + TIME_OUT;
        boolean result = false;
        while (currentTime < outTime) {
            result = redisHook.setNx(lockKey, requestId, time, TimeUnit.MILLISECONDS);
            if (result) {
                return result;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("redis 加锁失败:{}",e.getMessage());
            }
            currentTime = System.currentTimeMillis();
        }
        return result;
    }

    /**
     * 解锁
     */
    public boolean unLock(String key, String requestId) {
        if (Strings.isBlank(key) || Strings.isBlank(requestId)) {
            throw new ShareLockException("分布式锁-解锁-参数异常");
        }
        try {
            String value = redisHook.get(key);
            if (requestId.equals(value)) {
                redisHook.del(key);
                return true;
            }
        } catch (Exception e) {
            log.error("redis 解锁失败:{}",e.getMessage());
        }
        return false;
    }

    /**
     * 尝试加锁
     */
    public boolean tryLock(String lockKey, String requestId, Long time) {
        if (Strings.isBlank(lockKey) || Strings.isBlank(requestId) || time <= 0) {
            throw new ShareLockException("分布式锁-尝试加锁参数异常");
        }
        return redisHook.setNx(lockKey, requestId, time, TimeUnit.MILLISECONDS);
    }

}
