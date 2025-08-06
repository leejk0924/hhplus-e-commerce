package kr.hhplus.be.server.order.domain.entity;

import kr.hhplus.be.server.stub.StubProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class OrderTest {
    @DisplayName("[Domain:단위테스트] : 주문에 상품 추가 기능 유효성 확인")
    @Test
    void 주문에_상품_추가_테스트() throws Exception {
        // Given
        Order order = Order.of(1L, "주문");

        StubProduct product1 = new StubProduct(1L, "테스트1", 2500, 100);
        StubProduct product2 = new StubProduct(3L, "테스트2", 4000, 100);

        OrderItem orderItem1 = OrderItem.of(order, product1, 2, 2500);
        OrderItem orderItem2 = OrderItem.of(order, product2, 5, 4000);

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        int expectedPrice = orderItems.stream().mapToInt(it -> it.getQuantity() * it.getUnitPrice()).sum();

        // When
        order.addOrderItems(orderItems);

        // Then
        assertThat(order.getOrderItems().size()).isEqualTo(orderItems.size());
        assertThat(order.getProductTotalAmount()).isEqualTo(expectedPrice);
    }
}