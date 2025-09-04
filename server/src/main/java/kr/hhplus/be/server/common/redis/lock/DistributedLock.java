package kr.hhplus.be.server.common.redis.lock;

import kr.hhplus.be.server.common.redis.lock.key.DefaultLockKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String[] key() default {};
    Class<? extends LockKeyGenerator> keyGenerator() default DefaultLockKeyGenerator.class;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    long waitTime()default 5L;
    long leaseTime() default 3L;
}
