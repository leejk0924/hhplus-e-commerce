package kr.hhplus.be.server.coupon.presentation.controller;

import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.presentation.controller.spec.CouponApiSpec;
import kr.hhplus.be.server.coupon.presentation.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponController implements CouponApiSpec {
    private final CouponFacade couponFacade;
    @GetMapping("/coupon/{couponId}/first-come/users/{userId}")
    public ResponseEntity<CouponResponse.IssueCouponQueueDto> issueCouponFirstCome(
            @PathVariable Long userId,
            @PathVariable Long couponId
    ) {
        Integer waitingNum = couponFacade.enterCouponQueue(userId, couponId);
        return ResponseEntity.ok(CouponResponse.IssueCouponQueueDto.of(waitingNum));
    }
}
