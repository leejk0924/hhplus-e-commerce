package kr.hhplus.be.server.order.application.facade;

import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderEntityRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderItemEntityRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
import kr.hhplus.be.server.testsupport.AbstractIntegrationTest;
import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentFacadeTest extends AbstractIntegrationTest {
    @Autowired
    private PaymentFacade sut;
    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Autowired
    private OrderItemEntityRepository orderItemEntityRepository;
    @Autowired
    private UsersEntityRepository usersEntityRepository;
    private List<User> users;
    private List<Order> orders;
    private List<OrderItem> orderItems;
    private Product product;
    int initStockQuantity = 100;

    @BeforeEach
    void setUp() {
        users = IntStream.range(0, 50).mapToObj(
                i -> usersEntityRepository.save(User.of(null, "테스트 계정" + i, 100_000))
        ).toList();

        orders = IntStream.range(0, 50).mapToObj(
                i -> orderEntityRepository.save(Order.of(users.get(i).getId(), "주문상태"))
        ).toList();

        product = Product.of("테스트 상품1", 1000, initStockQuantity);
        productEntityRepository.save(product);

        orderItems = orders.stream().map(
                o -> orderItemEntityRepository.save(OrderItem.of(o, product, 2, 2500))
        ).toList();
    }

    @DisplayName("[통합테스트:동시성이슈] : 결제 시, 상품의 재고 동시성 이슈 테스트")
    @Test
    void 결제_동시성_성공_테스트() throws Exception {
        // Given
        int threadCount = users.size();
        int expectedStockQuantity = initStockQuantity - orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        for (int i = 0; i < threadCount; i++) {
            final Long userId = users.get(i).getId();
            final long orderId = orders.get(i).getId();
            executorService.execute(() -> {
                try {
                    PayCommand payCommand = new PayCommand(userId, orderId, null);
                    sut.payProcess(payCommand);
                    barrier.await();
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
                () -> assertThat(successCount.get())
                        .as("결제 성공 후 상품 차감 성공 검증")
                        .isEqualTo(threadCount),
                () -> assertThat(failCount.get())
                        .as("상품 차감 실패 검증")
                        .isZero(),
                () -> assertThat(target.getStockQuantity())
                        .as("재고 차감")
                        .isEqualTo(expectedStockQuantity)
        );
    }
}