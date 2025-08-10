package kr.hhplus.be.server.coupon.application.facade;

import kr.hhplus.be.server.AbstractIntegrationTest;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.persistence.jpa.CouponEntityRepository;
import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CouponFacadeConcurrencyTest extends AbstractIntegrationTest {
    @Autowired
    private CouponFacade sut;
    @Autowired
    private CouponEntityRepository couponEntityRepository;
    @Autowired
    private UsersEntityRepository usersEntityRepository;

    private Coupon coupon;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = IntStream.range(0, 10).mapToObj(
                i -> usersEntityRepository.save(User.of(null, "테스트 계정" + i, 100_000))
        ).toList();
        coupon = Coupon.of(null, "테스트 쿠폰", "고정", 1000, 100);
        couponEntityRepository.save(coupon);
    }

    @DisplayName("[통합테스트:동시성이슈] : 쿠폰 발급 시, 동시성 이슈 테스트")
    @Test
    void 쿠폰_발급_동시성_테스트() throws Exception {
        // Given
        Long couponId = coupon.getId();
        int threadCount = users.size();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        for (int i = 0; i < threadCount; i++) {
            final Long userId = users.get(i).getId();
            executorService.execute(() -> {
                try {
                    barrier.await();
                    sut.issuedCoupon(userId, couponId);
                    successCount.incrementAndGet();
                } catch (PessimisticLockingFailureException e) {
                    failCount.incrementAndGet();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new IllegalStateException("Barrier Synchronization failed");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(3, TimeUnit.SECONDS);

        // Then
        Coupon coupon = couponEntityRepository.findById(couponId).orElseThrow();
        assertAll(
                () -> assertThat(successCount.get())
                        .as("쿠폰 발급 성공")
                        .isEqualTo(threadCount),
                () -> assertThat(failCount.get())
                        .as("쿠폰 발급 실패")
                        .isEqualTo(0),
                () -> assertThat(coupon.getCouponInventory())
                        .as("남은 쿠폰 갯수")
                        .isEqualTo(90)
        );
    }
}