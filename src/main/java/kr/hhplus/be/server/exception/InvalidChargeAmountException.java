package kr.hhplus.be.server.exception;

import lombok.Getter;

@Getter
public class InvalidChargeAmountException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidChargeAmountException() {
        super(UserErrorCode.INVALID_CHARGE_POINT.getMessage());
        this.errorCode = UserErrorCode.INVALID_CHARGE_POINT;
    }
}
