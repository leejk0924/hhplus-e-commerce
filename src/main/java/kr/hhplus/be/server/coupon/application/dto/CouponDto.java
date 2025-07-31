package kr.hhplus.be.server.coupon.application.dto;

import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;

import java.time.LocalDateTime;

public record CouponDto(
        Long couponId,
        String couponName,
        String discountType,
        int discountRate,
        LocalDateTime expiredAt
) {
    public static CouponDto from (Coupon coupon, UserCoupon userCoupon) {
        return new CouponDto(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscountType(),
                coupon.getDiscountRate(),
                userCoupon.getExpiredAt());
    }

}
