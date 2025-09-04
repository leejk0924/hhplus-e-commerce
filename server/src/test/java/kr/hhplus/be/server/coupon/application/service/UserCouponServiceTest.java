package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.coupon.application.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.stub.StubUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {
    @InjectMocks
    private UserCouponService sut;
    @Mock
    private UserCouponRepository userCouponRepository;

    @Test
    void 쿠폰_중복_확인_예외_테스트() throws Exception {
        // Given
        StubUser doubleUser = StubUser.of(1L, "test", 1000);
        Coupon coupon = Coupon.of( 1L, "테스트 쿠폰", "고정할인", 2000, 30);
        given(userCouponRepository.hasCoupon(anyLong(), anyLong())).willReturn(true);

        // When && Then
        assertThatExceptionOfType(RestApiException.class)
                .isThrownBy(()->sut.validNotDuplicate(doubleUser.getId(), coupon.getId()))
                .withMessage(CouponErrorCode.DUPLICATE_USER_COUPON.getMessage());
    }
    @Test
    void 유저_쿠폰_발급_성공_테스트() throws Exception {
        // Given
        long couponId = 1L;
        long userId = 1L;
        Coupon coupon = Coupon.of(couponId, "테스트 쿠폰", "고정", 1000, 100);
        UserCoupon issuedCoupon = UserCoupon.of(userId, coupon, "발급", null, LocalDateTime.now().plusDays(30));
        given(userCouponRepository.issueCoupon(any(UserCoupon.class))).willReturn(issuedCoupon);

        // When
        UserCoupon target = sut.issueCoupon(userId, coupon);

        // Then
        verify(userCouponRepository).issueCoupon(any(UserCoupon.class));
        assertThat(target).isEqualTo(issuedCoupon);
    }
}