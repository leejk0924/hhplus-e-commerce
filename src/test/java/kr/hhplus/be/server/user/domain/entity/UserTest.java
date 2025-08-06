package kr.hhplus.be.server.user.domain.entity;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserTest {
    @DisplayName("[Domain:단위테스트] : 포인트가 null 일 경우 0으로 반환")
    @ParameterizedTest(name = "point={0} -> expected={1}")
    @CsvSource({
            ", 0",
            "10_000_000, 10_000_000"
    })
    void 유저_보유포인트_성공_케이스(Integer point, Integer expected) throws Exception {
        // Given
        long userId = 1L;
        User user = User.of(userId, "test", point);

        // When && Then
        assertThat(user.hasBalanced()).isEqualTo(expected);
    }

    @DisplayName("[Domain:단위테스트] : 보유 포인트가 유효하지 않은 경우")
    @ParameterizedTest
    @MethodSource("포인트_유효성_테스트_케이스")
    void 유저_포인트_유효성_예외_테스트(
            int point,
            String errorMessage
    ) throws Exception {
        // Given
        long userId = 1L;

        // When && Then
        assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(()->User.of(userId, "test", point))
                .withMessage(errorMessage);
    }

    static Stream<Arguments> 포인트_유효성_테스트_케이스() {
        return Stream.of(
                Arguments.of( 10_000_001, UserErrorCode.INVALID_POINT.getMessage()),
                Arguments.of( -1, UserErrorCode.INVALID_POINT.getMessage())
        );
    }
    @DisplayName("[Domain:단위테스트] : 포인트 충전 성공 테스트")
    @Test
    void 포인트_충전_성공_테스트() throws Exception {
        // Given
        long userId = 1L;
        int balancePoint = 1;
        int chargePoint = 1;
        int expect = balancePoint + chargePoint;
        User target = User.of(userId, "test", chargePoint);

        // When
        target.chargePoint(chargePoint);

        // Then
        assertThat(target.hasBalanced()).isEqualTo(expect);
    }
    @DisplayName("[Domain:단위테스트] : 포인트 충전시 예외 테스트")
    @ParameterizedTest
    @MethodSource("포인트_충전_예외_테스트_케이스")
    void 포인트_충전_실패_테스트(
            int balancePoint,
            int chargePoint,
            String errorMessage
    ) throws Exception {
        // Given
        long userId = 1L;
        User target = User.of(userId, "test", balancePoint);
        // When && Then
        assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(()->target.chargePoint(chargePoint))
                .withMessage(errorMessage);
    }
    static Stream<Arguments> 포인트_충전_예외_테스트_케이스() {
        return Stream.of(
                Arguments.of(1, 10_000_001, UserErrorCode.INVALID_CHARGE_POINT.getMessage()),
                Arguments.of( 1, -1, UserErrorCode.INVALID_CHARGE_POINT.getMessage()),
                Arguments.of( 5_000_000, 5_000_001, UserErrorCode.EXCEEDED_POINT.getMessage())
        );
    }
}