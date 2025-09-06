package kr.hhplus.be.server.coupon.domain.entity;

import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CouponTest {
    @DisplayName("[Domain:단위테스트] : 쿠폰 발급 시 재고 차감 성공 테스트")
    @Test
    void 쿠폰_재고_차감_성공_테스트() throws Exception {
        // Given
        int discountRate = 1000;
        int couponInventory = 100;
        int expected = couponInventory - 1;
        Coupon target = Coupon.of(1L, "테스트 쿠폰", "고정할인", discountRate, couponInventory);

        // When
        target.issue();

        // Then
        assertThat(target.getCouponInventory()).isEqualTo(expected);
    }
    @DisplayName("[Domain:단위테스트] : 쿠폰 발급 시 재고 차감 실패 테스트")
    @Test
    void 쿠폰_재고_차감_실패_테스트() throws Exception {
        // Given
        int discountRate = 1000;
        int couponInventory = 0;
        Coupon target = Coupon.of(1L, "테스트 쿠폰", "고정할인", discountRate, couponInventory);

        // When && Then
        assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(target::issue)
                .withMessage(CouponErrorCode.COUPON_OUT_OF_STOCK.getMessage());
    }
}