package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.dto.OrderItemCommand;
import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.stub.StubProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {
    @InjectMocks
    private OrderItemService sut;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Test
    void 상품_아이템_조회_테스트() throws Exception {
        // Given
        OrderItemCommand.ProductCommand productCommand1 = new OrderItemCommand.ProductCommand(1L, 10);
        OrderItemCommand.ProductCommand productCommand2 = new OrderItemCommand.ProductCommand(2L, 20);
        OrderItemCommand.ProductCommand productCommand3 = new OrderItemCommand.ProductCommand(3L, 30);
        List<OrderItemCommand.ProductCommand> productCommands = List.of(productCommand1, productCommand2, productCommand3);

        OrderItemCommand orderItemCommand = new OrderItemCommand(productCommands);

        Product product1 = StubProduct.of(1L, "product1", 1000, 10);
        Product product2 = StubProduct.of(2L, "product2", 3000, 50);
        Product product3 = StubProduct.of(3L, "product3", 6000, 100);

        OrderItem orderItem1 = OrderItem.of(null, product1, 1, 2500);
        OrderItem orderItem2 = OrderItem.of(null, product2, 2, 5000);
        OrderItem orderItem3 = OrderItem.of(null, product3, 3, 7000);

        List<OrderItem> orders = List.of(orderItem1, orderItem2, orderItem3);
        given(orderItemRepository.findAllOrderItems(anyList())).willReturn(orders);

        // When
        //TODO
        boolean target = sut.deductProducts(List.of());

        // Then
        assertThat(target).isTrue();
    }
}