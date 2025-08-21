package kr.hhplus.be.server.coupon.presentation.dto;

import kr.hhplus.be.server.coupon.application.dto.CouponDto;

import java.time.LocalDateTime;

public class CouponResponse {
    public record IssueCouponDto(
            Long couponId,
            String couponName,
            String discountType,
            int discountRate,
            LocalDateTime expiredAt
    ) {
        public IssueCouponDto (CouponDto couponDto){
            this(
                    couponDto.couponId(),
                    couponDto.couponName(),
                    couponDto.discountType(),
                    couponDto.discountRate(),
                    couponDto.expiredAt()
            );
        }
        public static IssueCouponDto fromCouponDto(CouponDto couponDto) {
            return new IssueCouponDto(couponDto);
        }
    }
    public record IssueCouponQueueDto(
            String message
    ) {
        public static IssueCouponQueueDto of(int queueNo) {
            return new IssueCouponQueueDto("현재 쿠폰 발급 대기열은 '" + queueNo + "'번째 입니다.");
        }
    }
}
