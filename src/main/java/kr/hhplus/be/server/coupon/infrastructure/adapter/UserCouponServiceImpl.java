package kr.hhplus.be.server.coupon.infrastructure.adapter;

import kr.hhplus.be.server.coupon.application.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.coupon.infrastructure.persistence.jpa.UserCouponEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponRepository {
    private final UserCouponEntityRepository userCouponEntityRepository;

    @Override
    public boolean hasCoupon(Long userId, Long couponId) {
        return userCouponEntityRepository.existsByUserAndCoupon(userId, couponId);
    }

    @Override
    public UserCoupon issueCoupon(UserCoupon issuedCoupon) {
        return userCouponEntityRepository.save(issuedCoupon);
    }
}
