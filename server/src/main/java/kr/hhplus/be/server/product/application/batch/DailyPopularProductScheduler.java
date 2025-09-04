package kr.hhplus.be.server.product.application.batch;

import kr.hhplus.be.server.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyPopularProductScheduler {
    private final ProductService productService;
    @Scheduled(cron = "5 0 0 * * *", zone = "Asia/Seoul")
    @SchedulerLock(
            name = "LOCK:DAILY_POP_PRODUCT",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT10M"
    )
    public void aggregateDailyPopularProducts() {
        productService.refreshPopularProduct();
    }
}
