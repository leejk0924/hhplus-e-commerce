package kr.hhplus.be.server.coupon.infrastructure.adapter;

import kr.hhplus.be.server.coupon.application.repository.CouponIssuedRepository;
import kr.hhplus.be.server.coupon.infrastructure.persistence.redis.RedisCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssuedRepositoryImpl implements CouponIssuedRepository {
    private final RedisCouponRepository redisCouponRepository;

    @Override
    public void markCouponIssued(String key, Long userId) {
        redisCouponRepository.markCouponIssued(key, userId);
    }

    @Override
    public boolean hasCouponIssued(String key, Long userId) {
        return redisCouponRepository.hasCouponIssued(key, userId);
    }

    @Override
    public int enrollAndGetRank(String key, Long userId) {
        return redisCouponRepository.enrollAndGetRank(key, userId);
    }

    @Override
    public List<String> scanKeys(String pattern) {
        return redisCouponRepository.scanKeys(pattern);
    }

    @Override
    public List<Long> popQueue(String queueKey) {
        return redisCouponRepository.popFromQueue(queueKey, 10);
    }
}
