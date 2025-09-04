package kr.hhplus.be.server.coupon.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserCouponTest {
    @DisplayName("[Domain:단위테스트] : 유저 쿠폰 발급 성공 테스트")
    @Test
    void 유저_쿠폰_발급_성공_테스트() throws Exception {
        // Given
//        StubUser doubleUser = StubUser.of(1L, "test", 1000);
        Long userId = 1L;
        Coupon coupon = Coupon.of( 1L, "쿠폰명", "고정할인", 2000, 30);
        String couponStatus = "발급됨";
        UserCoupon target = UserCoupon.of(
                userId,
                coupon,
                couponStatus,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now()
        );

        // When && Then
        assertThat(target.getUserId()).isEqualTo(userId);
        assertThat(target.getCouponId()).isEqualTo(coupon);
    }
}