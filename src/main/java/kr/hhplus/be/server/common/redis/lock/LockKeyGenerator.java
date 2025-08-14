package kr.hhplus.be.server.common.redis.lock;

import org.aspectj.lang.reflect.MethodSignature;

public interface LockKeyGenerator {
    String[] generateKey(MethodSignature signature, Object[] args);
}
