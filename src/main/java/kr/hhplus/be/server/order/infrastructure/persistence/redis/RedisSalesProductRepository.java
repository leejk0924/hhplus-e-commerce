package kr.hhplus.be.server.order.infrastructure.persistence.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisSalesProductRepository {
    private final StringRedisTemplate redisTemplate;

    public void updateSalesQuantity(String key, Map<String, Long> salesProducts, Duration ttl) {
        final var serializer = redisTemplate.getStringSerializer();
        final byte[] rawKey = serializer.serialize(key);
        final byte[] nx = "NX".getBytes(StandardCharsets.UTF_8);
        final byte[] seconds = String.valueOf(ttl.toSeconds()).getBytes(StandardCharsets.UTF_8);

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Map.Entry<String, Long> e : salesProducts.entrySet()) {
                byte[] rawMember = serializer.serialize(e.getKey());
                double delta = e.getValue().doubleValue();
                connection.zSetCommands().zIncrBy(rawKey, delta, rawMember);
            }
            connection.execute("EXPIRE", rawKey, seconds, nx);
            return null;
        });
    }
}
