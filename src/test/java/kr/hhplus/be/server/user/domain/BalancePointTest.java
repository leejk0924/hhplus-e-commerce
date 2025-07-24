package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.exception.ExceededPointBalanceException;
import kr.hhplus.be.server.exception.InvalidChargeAmountException;
import kr.hhplus.be.server.exception.InvalidPointAmountException;
import kr.hhplus.be.server.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BalancePointTest {
    @DisplayName("[Domain:단위테스트] : 포인트가 null 일 경우 0으로 반환")
    @ParameterizedTest(name = "point={0} -> expected={1}")
    @CsvSource({
            ", 0",
            "10_000_000, 10_000_000"
    })
    void 유저_보유포인트_성공_케이스(Integer point, Integer expected) throws Exception {
        // Given
        BalancePoint balancePoint = new BalancePoint(point);

        // When
        balancePoint.isValid();

        // Then
        assertThat(balancePoint.getBalance()).isEqualTo(expected);
    }

    @DisplayName("[Domain:단위테스트] : 보유 포인트가 유효하지 않은 경우")
    @ParameterizedTest
    @MethodSource("포인트_유효성_테스트_케이스")
    void 유저_포인트_유효성_예외_테스트(
            int point,
            Class<? extends RuntimeException> exception,
            String errorMessage
    ) throws Exception {
        // Given
        BalancePoint balancePoint = new BalancePoint(point);

        // When && Then
        assertThatExceptionOfType(exception)
                    .isThrownBy(balancePoint::isValid)
            .withMessage(errorMessage);
    }

    static Stream<Arguments> 포인트_유효성_테스트_케이스() {
        return Stream.of(
                Arguments.of( 10_000_001, InvalidPointAmountException.class, UserErrorCode.INVALID_POINT.getMessage()),
                Arguments.of( -1, InvalidPointAmountException.class, UserErrorCode.INVALID_POINT.getMessage())
        );
    }
    @DisplayName("[Domain:단위테스트] : 포인트 충전 성공 테스트")
    @Test
    void 포인트_충전_성공_테스트() throws Exception {
        // Given
        int balancePoint = 1;
        int chargePoint = 1;
        int expect = balancePoint + chargePoint;
        BalancePoint target = new BalancePoint(balancePoint);

        // When
        target.chargePoint(chargePoint);

        // Then
        assertThat(target.getBalance()).isEqualTo(expect);
    }
    @DisplayName("[Domain:단위테스트] : 포인트 충전시 예외 테스트")
    @ParameterizedTest
    @MethodSource("포인트_충전_예외_테스트_케이스")
    void 포인트_충전_실패_테스트(
            int balancePoint,
            int chargePoint,
            Class<? extends RuntimeException> exception,
            String errorMessage
    ) throws Exception {
        // Given
        BalancePoint target = new BalancePoint(balancePoint);

        // When && Then
        assertThatExceptionOfType(exception)
                .isThrownBy(()->target.chargePoint(chargePoint))
                .withMessage(errorMessage);
    }
    static Stream<Arguments> 포인트_충전_예외_테스트_케이스() {
        return Stream.of(
                Arguments.of(1, 10_000_001, InvalidChargeAmountException.class, UserErrorCode.INVALID_CHARGE_POINT.getMessage()),
                Arguments.of( 1, -1, InvalidChargeAmountException.class, UserErrorCode.INVALID_CHARGE_POINT.getMessage()),
                Arguments.of( 5_000_000, 5_000_001, ExceededPointBalanceException.class, UserErrorCode.EXCEEDED_POINT.getMessage())
        );
    }
}