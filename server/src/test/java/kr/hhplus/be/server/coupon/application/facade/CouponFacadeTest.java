package kr.hhplus.be.server.coupon.application.facade;

import kr.hhplus.be.server.coupon.application.dto.CouponDto;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponFacadeTest {
    @InjectMocks
    private CouponFacade sut;
    @Mock
    private UserCouponService userCouponService;
    @Mock
    private CouponService couponService;

    @Test
    void 쿠폰_발급_Facade_테스트() throws Exception {
        // Given
        Long userId = 1L;
        Long couponId = 1L;
        Coupon coupon = Coupon.of(1L, "test", "고정", 1000, 100);
//        given(userCouponService.validNotDuplicate(anyLong(), anyLong())).willReturn()
        given(couponService.hasRemainCoupon(anyLong())).willReturn(coupon);
        UserCoupon issuedUserCoupon = UserCoupon.of(userId, coupon, "발급", null, LocalDateTime.now().plusDays(30));
        given(userCouponService.issueCoupon(userId, coupon)).willReturn(issuedUserCoupon);
        // When
        CouponDto target = sut.issuedCoupon(userId, couponId);

        // Then
        verify(userCouponService).validNotDuplicate(userId, couponId);
        verify(userCouponService).issueCoupon(userId, coupon);
        assertThat(target.couponId()).isEqualTo(coupon.getId());
        assertThat(target.couponName()).isEqualTo(coupon.getCouponName());
        assertThat(target.discountRate()).isEqualTo(coupon.getDiscountRate());
        assertThat(target.expiredAt()).isEqualTo(issuedUserCoupon.getExpiredAt());
    }

}