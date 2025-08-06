package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.coupon.application.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public Coupon hasRemainCoupon(Long couponId) {
        Coupon coupon = couponRepository.searchCoupon(couponId);
        if (coupon.getCouponInventory() <= 0) {
            throw new RestApiException(CouponErrorCode.COUPON_OUT_OF_STOCK);
        }
        return coupon;
    }
}
