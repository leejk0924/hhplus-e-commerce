package kr.hhplus.be.server.product.infrastructure.persistence.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisProductRankingRepository {
    private final StringRedisTemplate redisTemplate;
    public void initZSet(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }

    public void unionZSets(String key, List<String> keys) {
        if(keys == null || keys.size() < 2) {
            return;
        }
        String firstKey = keys.get(0);
        List<String> otherKeys = keys.subList(1, keys.size());
        redisTemplate.opsForZSet().unionAndStore(firstKey, otherKeys, key);
        redisTemplate.expire(key, Duration.of(2, ChronoUnit.DAYS));
    }

    public Map<Long, Integer> getTopNProducts(String key, int count) {
        var rawResult = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, count - 1);
        if (rawResult == null || rawResult.isEmpty()) {
            return Map.of();
        }
        var result = new LinkedHashMap<Long, Integer>();
        for (ZSetOperations.TypedTuple<String> t : rawResult) {
            if (t.getValue() == null || t.getScore() == null) continue;
            try {
                long id = Long.parseLong(t.getValue());
                int score = (int) Math.round(t.getScore());
                result.put(id, score);
            } catch (NumberFormatException ignore) {
            }
        }
        return result;
    }
}
