package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "쿠폰의 재고가 부족합니다." ),
    INVALID_USER(HttpStatus.NOT_FOUND, "유저 ID는 null을 사용할 수 없습니다."),
    INVALID_COUPON(HttpStatus.NOT_FOUND, "쿠폰 ID는 null을 사용할 수 없습니다."),
    DUPLICATE_USER_COUPON(HttpStatus.CONFLICT, "사용자는 해당 쿠폰을 이미 보유하고 있습니다."),
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "쿠폰 ID가 존재하지 않습니다."),
    EXPIRED_COUPON(HttpStatus.NOT_FOUND, "해당 쿠폰은 사용기간이 만료되었습니다."),
    ALREADY_REDEEMED(HttpStatus.NOT_FOUND,"해당 쿠폰은 이미 사용되었습니다."),
    FAIL_ENROLL_COUPON(HttpStatus.INTERNAL_SERVER_ERROR, "쿠폰 발급 요청이 실패하였습니다."),
    ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "해당 쿠폰을 이미 발급 받으셨습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus statusCode() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
