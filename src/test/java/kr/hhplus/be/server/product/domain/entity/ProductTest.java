package kr.hhplus.be.server.product.domain.entity;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.product.exception.ProductErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {
    @DisplayName("[Domain:단위테스트] : 보유 포인트가 유효하지 않은 경우")
    @ParameterizedTest
    @MethodSource("상품_유효성_예외_테스트_케이스")
    void 상품_유효성_예외_실패_테스트(
            String productName,
            int price,
            int stockQuantity,
            String errorMessage
    ) throws Exception {
        // Given
        long productId = 1L;
        
        // When && Then
        Assertions.assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(() -> Product.of(productId, productName, price, stockQuantity))
                .withMessage(errorMessage);
    }
    static Stream<Arguments> 상품_유효성_예외_테스트_케이스() {
        return Stream.of(
                Arguments.of(null, 1000, 30, ProductErrorCode.PRODUCT_NAME_NOT_FOUND.getMessage()),
                Arguments.of("", 1000, 30, ProductErrorCode.PRODUCT_NAME_NOT_FOUND.getMessage()),
                Arguments.of("test", 1000, -1, ProductErrorCode.INVALID_STOCK_QUANTITY.getMessage()),
                Arguments.of("test", -1, 30, ProductErrorCode.INVALID_PRICE.getMessage()),
                Arguments.of("test", 10_000_001, 30, ProductErrorCode.INVALID_PRICE.getMessage())
        );
    }
    @Test
    void 상품_차감_성공_테스트() throws Exception {
        // Given
        int initQuantity = 200;
        int decreaseStockQuantity = 100;
        int expected = initQuantity - decreaseStockQuantity;
        Product target = Product.of(1L, "테스트 상품1", 4000, initQuantity);

        // When
        target.decreaseStockQuantity(decreaseStockQuantity);

        // Then
        assertThat(target.getStockQuantity()).isEqualTo(expected);
    }
}