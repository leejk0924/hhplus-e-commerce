package kr.hhplus.be.server.coupon.application.facade;

import kr.hhplus.be.server.common.redis.DistributedLock;
import kr.hhplus.be.server.coupon.application.dto.CouponDto;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFacade {
    private final UserCouponService userCouponService;
    private final CouponService couponService;

    @DistributedLock(key = "'COUPON:'+ #couponId")
    public CouponDto issuedCoupon(Long userId, Long couponId) {
        userCouponService.validNotDuplicate(userId, couponId);
        Coupon coupon = couponService.hasRemainCoupon(couponId);
        coupon.issue();
        UserCoupon userCoupon = userCouponService.issueCoupon(userId, coupon);
        return CouponDto.from(coupon, userCoupon);
    }
}
