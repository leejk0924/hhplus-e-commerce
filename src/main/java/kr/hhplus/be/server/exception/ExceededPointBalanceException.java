package kr.hhplus.be.server.exception;

import lombok.Getter;

@Getter
public class ExceededPointBalanceException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExceededPointBalanceException() {
        super(UserErrorCode.EXCEEDED_POINT.getMessage());
        this.errorCode = UserErrorCode.EXCEEDED_POINT;
    }
}
