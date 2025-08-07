package kr.hhplus.be.server.coupon.infrastructure.adapter;

import kr.hhplus.be.server.coupon.application.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.coupon.infrastructure.persistence.jpa.CouponEntityRepository;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponEntityRepository couponEntityRepository;

    @Override
    public Coupon searchCoupon(Long couponId) {
        return couponEntityRepository.findByIdForUpdate(couponId).orElseThrow(() -> new RestApiException(CouponErrorCode.NOT_FOUND_COUPON));
    }
}
