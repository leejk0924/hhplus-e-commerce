package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.AbstractIntegrationTest;
import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderEntityRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderItemEntityRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderItemServiceConcurrencyTest extends AbstractIntegrationTest {
    @Autowired
    private OrderItemService sut;
    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Autowired
    private OrderItemEntityRepository orderItemEntityRepository;
    @Autowired
    private UsersEntityRepository usersEntityRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        User user = User.of(null, "테스트 계정", 10000);
        usersEntityRepository.save(user);

        Order order = Order.of(user.getId(), "주문상태");
        orderEntityRepository.save(order);

        product = Product.of("테스트 상품1", 1000, 10);
        productEntityRepository.save(product);

        OrderItem orderItem = OrderItem.of(order, product, 1, 2500);
        orderItemEntityRepository.save(orderItem);
    }

    @DisplayName("[통합테스트:동시성이슈] : 상품의 재고 차감 동시성 테스트")
    @Test
    void 상품_재고_차감_테스트() throws Exception {
        // Given
        int threadCount = product.getStockQuantity();
        List<Long> productIds = List.of(product.getId());

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    barrier.await();
                    sut.deductProducts(productIds);
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
        Product target = productEntityRepository.findById(product.getId()).orElseThrow();

        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(threadCount),
                () -> assertThat(failCount.get()).isEqualTo(0),
                () -> assertThat(target.getStockQuantity()).isEqualTo(0)
        );
    }
}
