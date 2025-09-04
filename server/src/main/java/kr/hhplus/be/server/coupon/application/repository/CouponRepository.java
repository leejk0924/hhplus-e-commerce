package kr.hhplus.be.server.coupon.application.repository;

import kr.hhplus.be.server.coupon.domain.entity.Coupon;

public interface CouponRepository {
    Coupon searchCoupon(Long couponId);
}
