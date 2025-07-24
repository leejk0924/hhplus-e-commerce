package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.exception.InvalidPointAmountException;
import kr.hhplus.be.server.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
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
}