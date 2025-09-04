package kr.hhplus.be.server.product.exception;

import kr.hhplus.be.server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "상품명에 대한 정보가 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 ID에 대한 상품정보를 찾을수가 없습니다."),
    INVALID_STOCK_QUANTITY(HttpStatus.BAD_REQUEST, "상품의 재고가 유효하지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "상품의 금액이 유효하지 않습니다.");
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
