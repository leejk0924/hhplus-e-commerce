package kr.hhplus.be.server.product.infrastructure.adapter;

import kr.hhplus.be.server.product.application.repository.ProductRankingRepository;
import kr.hhplus.be.server.product.infrastructure.persistence.redis.RedisProductRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class ProductRankingRepositoryImpl implements ProductRankingRepository {
    private final RedisProductRankingRepository redisProductRankingRepository;
    @Value("${db.product.popular}")
    private String baseKey;
    @Value("${db.product.ranking}")
    private String rankingKey;
    @Override
    public void initSortedSet() {
        String key = baseKey + ":" + LocalDate.now();
        Duration ttl = Duration.of(4, ChronoUnit.DAYS);
        redisProductRankingRepository.initZSet(key, ttl);
    }

    @Override
    public void unionLast3Days() {
        LocalDate today = LocalDate.now();
        String key = rankingKey + ":" + LocalDate.now();
        List<String> keys = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> baseKey + ":" + today.minusDays(i))
                .toList();
        redisProductRankingRepository.unionZSets(key, keys);
    }

    @Override
    public Map<Long, Integer> getTopNProducts() {
        String key = rankingKey + ":" + LocalDate.now();
        return redisProductRankingRepository.getTopNProducts(key, 5);
    }
}
