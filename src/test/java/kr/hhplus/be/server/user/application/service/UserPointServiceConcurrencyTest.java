package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.AbstractIntegrationTest;
import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserPointServiceConcurrencyTest extends AbstractIntegrationTest {
    @Autowired
    private UserPointService sut;
    @Autowired
    private UsersEntityRepository usersEntityRepository;

    private User user;
    @BeforeEach
    void setUp() {
        user = User.of(null, "테스트 계정", 0);
        usersEntityRepository.save(user);
    }

    @DisplayName("[통합테스트:동시성이슈] : 동시에 포인트 충전 시, 최초 하나의 요청만 성공")
    @Test
    void 포인트_충전_동시성_테스트() throws Exception {
        // Given
        Long userId = user.getId();
        int chargePoint = 1000;
        int threadCount = 2;
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(()->{
                 try {
                     barrier.await();
                     sut.chargePoint(userId, chargePoint);
                     successCount.incrementAndGet();
                 } catch (OptimisticLockingFailureException e) {
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
        User updatedUser = usersEntityRepository.findById(userId).orElseThrow();

        assertAll(
                ()->assertThat(successCount.get())
                        .as("성공한 충전 요청 수는 1이어야 한다.")
                        .isEqualTo(1),
                ()-> assertThat(failCount.get())
                        .as("실패한 충전 요청 수는 1이어야 한다.")
                        .isEqualTo(1),
                ()-> assertThat(updatedUser.getPointBalance())
                        .as("충전 후 잔액 결과")
                        .isEqualTo(1000)
        );
    }
}
