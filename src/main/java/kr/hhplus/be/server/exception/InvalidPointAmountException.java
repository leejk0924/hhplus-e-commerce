package kr.hhplus.be.server.exception;

import lombok.Getter;

@Getter
public class InvalidPointAmountException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidPointAmountException() {
        super(UserErrorCode.INVALID_POINT.getMessage());
        this.errorCode = UserErrorCode.INVALID_POINT;
    }
}
