package kr.hhplus.be.server.coupon.infrastructure.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueEvent {
    private Long couponId;
    private Long userId;
    private LocalDateTime requestedAt;
    
    public static CouponIssueEvent of(Long couponId, Long userId) {
        return new CouponIssueEvent(couponId, userId, LocalDateTime.now());
    }
}