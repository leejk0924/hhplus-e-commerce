package kr.hhplus.be.server.coupon.application.batch;

import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.application.repository.CouponIssuedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
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
