package kr.hhplus.be.server.common.redis;

import kr.hhplus.be.server.common.redis.key.DefaultLockKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;
    private final ApplicationContext applicationContext;

    @Around("@annotation(distributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint, final DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] sortedKeys;
        if (distributedLock.key().length > 0) {
            DefaultLockKeyGenerator defaultGenerator = applicationContext.getBean(DefaultLockKeyGenerator.class);
            sortedKeys = defaultGenerator.generateKey(signature, joinPoint.getArgs());
        } else {
            LockKeyGenerator generator = applicationContext.getBean(distributedLock.keyGenerator());
            sortedKeys = generator.generateKey(signature, joinPoint.getArgs());
        }

        RLock[] locks = Arrays.stream(sortedKeys)
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RLock rLock = new RedissonMultiLock(locks);

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if(!available) {
                throw new InterruptedException();
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}",
                        signature.getMethod().getName(),
                        Arrays.toString(sortedKeys)
                );
            }
        }
    }
}
