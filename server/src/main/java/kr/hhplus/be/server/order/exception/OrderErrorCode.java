package kr.hhplus.be.server.order.exception;

import kr.hhplus.be.server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_STATUS_ERROR(HttpStatus.NOT_FOUND, "주문 상태를 찾을수가 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "주문 상품의 수량은 최소 0개 입니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "상품의 최소 가격은 0원 입니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다."),
    INVALID_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다.");
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
