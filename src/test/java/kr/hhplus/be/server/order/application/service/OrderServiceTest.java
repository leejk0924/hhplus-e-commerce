package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.dto.OrderItemCommand;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.stub.StubProduct;
import kr.hhplus.be.server.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService sut;
    @Mock
    private OrderRepository orderRepository;

    @Test
    void 주문_생성_테스트() throws Exception {
        // Given
        StubProduct product1 = StubProduct.of(1L, "상품1", 1000, 100);
        StubProduct product2 = StubProduct.of(2L, "상품2", 2000, 200);
        List<Product> products = List.of(product1, product2);
        OrderItemCommand.ProductCommand productCommand1 = new OrderItemCommand.ProductCommand(1L, 100);
        OrderItemCommand.ProductCommand productCommand2 = new OrderItemCommand.ProductCommand(2L, 200);
        List<OrderItemCommand.ProductCommand> productCommands = List.of(productCommand1, productCommand2);

        // When
        Order target = sut.createOrder(products, productCommands, 1L);

        // Then
        assertThat(target.getOrderItems().size()).isEqualTo(products.size());
        assertThat(target.getOrderStatus()).isEqualTo("주문상태");

        assertThat(target.getOrderItems().get(0).getProduct().getId()).isEqualTo(product1.getId());
        assertThat(target.getOrderItems().get(1).getProduct().getId()).isEqualTo(product2.getId());
    }
    
    @Test
    void 포인트_차감_성공_테스트() throws Exception {
        // Given
        Long userId = 1L;
        int initBalance = 30000;

        User user = User.of(userId, "test1", initBalance);

        Order order = Order.of(userId, "주문상태");
        StubProduct product1 = StubProduct.of(1L, "상품1", 1000, 100);
        StubProduct product2 = StubProduct.of(2L, "상품2", 3000, 100);

        OrderItem orderItem1 = OrderItem.of(order, product1, 5, 1000);
        OrderItem orderItem2 = OrderItem.of(order, product2, 10, 2000);
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        order.addOrderItems(orderItems);

        int expectedBalance = initBalance - order.getPaymentAmount();

        PayCommand payCommand = new PayCommand(userId, 1L, 1L);
        given(orderRepository.findUserById(anyLong())).willReturn(user);

        // When
        boolean result = sut.withdrawPayment(payCommand, order);

        // Then
        assertThat(result).isTrue();
        assertThat(user.hasBalanced()).isEqualTo(expectedBalance);
    }
}