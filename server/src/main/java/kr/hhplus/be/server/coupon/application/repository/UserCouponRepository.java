package kr.hhplus.be.server.coupon.application.repository;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;

public interface UserCouponRepository {
    boolean hasCoupon(Long userId, Long couponId);
    UserCoupon issueCoupon(UserCoupon issuedCoupon);
}
