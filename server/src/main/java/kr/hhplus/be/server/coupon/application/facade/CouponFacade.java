package kr.hhplus.be.server.coupon.application.facade;

import kr.hhplus.be.server.common.redis.lock.DistributedLock;
import kr.hhplus.be.server.coupon.application.dto.CouponDto;
import kr.hhplus.be.server.coupon.application.repository.CouponIssuedRepository;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponFacade {
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final CouponIssuedRepository couponIssuedRepository;
    @Value("${db.coupon.queue}")
    private String baseQueueKey;
    @Value("${db.coupon.issued}")
    private String baseIssuedKey;

    @DistributedLock(key = "'COUPON:'+ #couponId")
    public CouponDto issuedCoupon(Long userId, Long couponId) {
        Coupon coupon = couponService.hasRemainCoupon(couponId);
        userCouponService.validNotDuplicate(userId, couponId);
        coupon.issue();
        UserCoupon userCoupon = userCouponService.issueCoupon(userId, coupon);
        return CouponDto.from(coupon, userCoupon);
    }

    public Integer enterCouponQueue(Long userId, Long couponId) {
        String issuedKey = baseIssuedKey + ":" + couponId;
        String couponQueueKey = baseQueueKey + ":" + couponId;
        if (couponIssuedRepository.hasCouponIssued(issuedKey, userId)) {
            throw new RestApiException(CouponErrorCode.ALREADY_ISSUED);
        }
        return couponIssuedRepository.enrollAndGetRank(couponQueueKey, userId);
    }

    public void processCouponIssueQueue(String couponKey) {
        String[] splitQueueKey = couponKey.split(":");
        Long couponId = Long.valueOf(splitQueueKey[splitQueueKey.length - 1]);
        List<Long> userIds = couponIssuedRepository.popQueue(couponKey);
        Coupon coupon = couponService.hasRemainCoupon(couponId);
        userIds.forEach(userId -> {
            userCouponService.validNotDuplicate(userId, couponId);
            coupon.issue();
            userCouponService.issueCoupon(userId, coupon);
            String issuedKey = baseIssuedKey + ":" + couponId;
            couponIssuedRepository.markCouponIssued(issuedKey, userId);
        });
    }
}
