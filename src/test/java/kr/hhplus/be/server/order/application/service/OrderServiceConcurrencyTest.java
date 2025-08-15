package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.testsupport.AbstractIntegrationTest;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.stub.StubProduct;
import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderServiceConcurrencyTest extends AbstractIntegrationTest {
    @Autowired
    private OrderService sut;
    @Autowired
    private UsersEntityRepository usersEntityRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.of(null, "테스트 계정", 10000);
        usersEntityRepository.save(user);
    }

    @DisplayName("[통합테스트:동시성이슈] : 포인트 차감 시, 최초 하나의 요청만 성공")
    @Test
    void 포인트_차감_동시성_테스트() throws Exception {
        // Given
        Long userId = user.getId();
        int threadCount = 2;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        PayCommand payCommand = new PayCommand(userId, 1L, null);
        Order order = Order.of(userId, "주문상태");

        Product product1 = StubProduct.of(1L, "product1", 1000, 10);
        Product product2 = StubProduct.of(2L, "product2", 3000, 50);
        OrderItem orderItem1 = OrderItem.of(null, product1, 2, 2500);
        OrderItem orderItem2 = OrderItem.of(null, product2, 1, 5000);

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        order.addOrderItems(orderItems);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    barrier.await();
                    sut.withdrawPayment(payCommand, order);
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
        User deductedUser = usersEntityRepository.findById(userId).orElseThrow();

        assertAll(
                () -> assertThat(successCount.get())
                        .as("성공한 포인트 차감 요청 수는 1이어야 한다.")
                        .isEqualTo(1),
                () -> assertThat(failCount.get())
                        .as("실패한 포인트 차감 요청 수는 1이어야 한다.")
                        .isEqualTo(1),
                () -> assertThat(deductedUser.getPointBalance())
                        .as("차감 후 잔액 결과")
                        .isEqualTo(0)
        );
    }
}
