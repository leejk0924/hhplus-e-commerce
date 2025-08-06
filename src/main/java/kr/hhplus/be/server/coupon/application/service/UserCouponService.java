package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.coupon.application.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public void validNotDuplicate(Long userId, Long couponId) {
        boolean hasCoupon = userCouponRepository.hasCoupon(userId, couponId);
        if (hasCoupon) {
            throw new RestApiException(CouponErrorCode.DUPLICATE_USER_COUPON);
        }
    }

    public UserCoupon issueCoupon(Long userId, Coupon couponId) {
        UserCoupon issuedCoupon = UserCoupon.of(userId, couponId, "발급", null, LocalDateTime.now().plusDays(30));
        return userCouponRepository.issueCoupon(issuedCoupon);
    }
}
