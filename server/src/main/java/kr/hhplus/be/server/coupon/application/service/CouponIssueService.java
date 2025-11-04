package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.infrastructure.kafka.common.Topics;
import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import kr.hhplus.be.server.coupon.infrastructure.kafka.dto.CouponIssueEvent;
import kr.hhplus.be.server.coupon.infrastructure.persistence.redis.RedisCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final RedisCouponRepository redisCouponRepository;
    private final KafkaRepository kafkaRepository;
    private final CouponService couponService;
    
    private static final String REDIS_KEY_PREFIX = "coupon:issued:";
    private static final String REDIS_QUEUE_KEY_PREFIX = "coupon:queue:";
    
    public void requestCouponIssue(Long couponId, Long userId) {
        CouponIssueEvent event = CouponIssueEvent.of(couponId, userId);
        kafkaRepository.sendMessage(
            Topics.COUPON_ISSUE.getTopicName(),
            String.valueOf(couponId),
            event
        );
        
        log.info("쿠폰 발급 이벤트 발행 완료. couponId: {}, userId: {}", couponId, userId);
    }
}