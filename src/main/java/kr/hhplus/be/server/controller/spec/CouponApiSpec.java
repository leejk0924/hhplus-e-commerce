package kr.hhplus.be.server.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.dto.CouponResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "쿠폰", description = "쿠폰 관련 API")
public interface CouponApiSpec {
    @Operation(summary = "쿠폰 선착순 발급")
    ResponseEntity<CouponResponse.issueCouponDto> issueCouponFirstCome(Long userId);
}
