package kr.hhplus.be.server.coupon.application.batch;

import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.application.repository.CouponIssuedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 더 이상 사용하지 않는 쿠폰 발급 스케줄러
 * <p>Kafka 기반 쿠폰 발급 로직으로 변경 완료</p>
 * @deprecated 사용하지 않는 스케줄러. 삭제 예정.
 */
@Deprecated
@Slf4j
@RequiredArgsConstructor
public class CouponIssueScheduler {
    private final CouponFacade couponFacade;
    private final CouponIssuedRepository couponIssuedRepository;
    @Value("${db.coupon.queue}")
    private String queueKey;
    @Scheduled(cron = "0/4 * * * * *", zone = "Asia/Seoul")
    @SchedulerLock(
            name = "LOCK:COUPON:ISSUED",
            lockAtLeastFor = "PT2S",
            lockAtMostFor = "PT10S"
    )

    public void aggregateIssueCoupon() {
        List<String> couponKeys = couponIssuedRepository.scanKeys(queueKey+ ":*");
        couponKeys.forEach(couponFacade::processCouponIssueQueue);
    }
}
