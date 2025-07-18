package kr.hhplus.be.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CouponResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class issueCouponDto {
        private Long couponId;
        private String couponName;
        private String discountType;
        private int discountRate;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }
}
