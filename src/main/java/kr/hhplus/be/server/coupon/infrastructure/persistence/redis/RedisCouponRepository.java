package kr.hhplus.be.server.coupon.infrastructure.persistence.redis;

import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisCouponRepository {
    private final StringRedisTemplate redisTemplate;

    public void markCouponIssued(String key, Long userId) {
        redisTemplate.opsForValue().setBit(key, userId, true);
    }

    public boolean hasCouponIssued(String key, Long userId) {
        Boolean bit = redisTemplate.opsForValue().getBit(key, userId);
        return bit != null && bit;
    }

    public int enrollAndGetRank(String key, Long userId) {
        long timestamp = System.currentTimeMillis();

        redisTemplate.opsForZSet().addIfAbsent(key, userId.toString(), timestamp);
        Long rank = redisTemplate.opsForZSet().rank(key, userId.toString());
        if (rank != null) {
            return rank.intValue();
        }
        throw new RestApiException(CouponErrorCode.FAIL_ENROLL_COUPON);
    }

    public List<String> scanKeys(String pattern) {
        List<String> keys = new ArrayList<>();

        redisTemplate.execute((RedisCallback<Object>) connection -> {
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(100)
                    .build();

            try (Cursor<byte[]> cursor = connection.scan(options)) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                log.error("Redis SCAN failed for pattern={}", pattern, e);
            }
            return null;
        });
        return keys;
    }

    public List<Long> popFromQueue(String couponQueueKey, int offset) {
        var users = redisTemplate.opsForZSet().popMin(couponQueueKey, offset);
        if (users == null || users.isEmpty()) {
            log.debug("큐가 비어있음: {}", couponQueueKey);
            throw new IllegalArgumentException("큐가 비었습니다.");
        }
        return users.stream()
                .map(u -> Long.valueOf(u.getValue().toString()))
                .collect(Collectors.toList());
    }
}
