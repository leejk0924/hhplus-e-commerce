package kr.hhplus.be.server.order.domain.entity;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.order.exception.OrderErrorCode;
import kr.hhplus.be.server.stub.StubProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderItemTest {

    @DisplayName("[Domain:단위테스트] : 주문이 유효하지 않은 경우")
    @ParameterizedTest
    @MethodSource("주문_유효성_예외_테스트_케이스")
    void 주문_상품_생성_유효성_실패_테스트(
            int stockQuantity,
            int quantity,
            int unitPrice,
            String errorMessage
    ) throws Exception {
        // Given
        StubProduct stubProduct = StubProduct.of(1L, "상품명", 2500, stockQuantity);

        // When && Then
        assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(() -> OrderItem.of(null, stubProduct, quantity, unitPrice))
                .withMessage(errorMessage);
    }
    static Stream<Arguments> 주문_유효성_예외_테스트_케이스() {
        return Stream.of(
                Arguments.of( 100, 30, -1, OrderErrorCode.INVALID_PRICE.getMessage()),
                Arguments.of(5, 10, 100, OrderErrorCode.OUT_OF_STOCK.getMessage()),
                Arguments.of( 100, 0, 100, OrderErrorCode.INVALID_QUANTITY.getMessage())
        );
    }
    @DisplayName("[Domain:단위테스트] : 상품의 재고 차감 성공 테스트")
    @Test
    void 주문_상품의_재고차감_성공_테스트() throws Exception {
        // Given
        int initStockQuantity = 100;
        int deductStockQuantity = 5;
        int expected = initStockQuantity - deductStockQuantity;
        StubProduct stubProduct1 = StubProduct.of(1L, "상품1", 2000, initStockQuantity);
        OrderItem orderItem1 = OrderItem.of(null, stubProduct1, deductStockQuantity, 2000);

        // When
        orderItem1.subtractProductStockQuantity();

        // Then
        assertThat(orderItem1.getProduct().getStockQuantity()).isEqualTo(expected);
    }
}