package kr.hhplus.be.server.coupon.presentation.controller;

import kr.hhplus.be.server.coupon.application.dto.CouponDto;
import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.presentation.controller.spec.CouponApiSpec;

import kr.hhplus.be.server.coupon.presentation.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponController implements CouponApiSpec {
    private final CouponFacade couponFacade;
    @GetMapping("/coupon/{couponId}/first-come/users/{userId}")
    public ResponseEntity<CouponResponse.IssueCouponDto> issueCouponFirstCome(
            @PathVariable Long userId,
            @PathVariable Long couponId
    ) {
        CouponDto couponDto = couponFacade.issuedCoupon(userId, couponId);
        return ResponseEntity.ok(CouponResponse.IssueCouponDto.fromCouponDto(couponDto));
    }
}
