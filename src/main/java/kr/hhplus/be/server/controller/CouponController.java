package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.controller.spec.CouponApiSpec;
import kr.hhplus.be.server.domain.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CouponController implements CouponApiSpec {
    @PostMapping("/coupon/first-come/users/{userId}")
    public ResponseEntity<CouponResponse.issueCouponDto> issueCouponFirstCome(@PathVariable Long userId) {
        CouponResponse.issueCouponDto sample = CouponResponse.issueCouponDto
                .builder()
                .couponId(1L)
                .couponName("항해 9기 기념 쿠폰")
                .discountType("고정")
                .discountRate(3000)
                .createAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(sample);
    }
}
