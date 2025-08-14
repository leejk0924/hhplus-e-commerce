package kr.hhplus.be.server.common.redis.key;

import kr.hhplus.be.server.common.parser.CustomSpringELParser;
import kr.hhplus.be.server.common.redis.DistributedLock;
import kr.hhplus.be.server.common.redis.LockKeyGenerator;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DefaultLockKeyGenerator implements LockKeyGenerator {
    @Override
    public String[] generateKey(MethodSignature signature, Object[] args) {
        DistributedLock annotation = signature.getMethod().getAnnotation(DistributedLock.class);
        return Arrays.stream(annotation.key())
                .map(k -> CustomSpringELParser.getDynamicValue(
                        signature.getParameterNames(),
                        args,
                        k
                ))
                .map(k -> "LOCK:" + k)
                .toArray(String[]::new);
    }
}
