package kr.hhplus.be.server.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND.getMessage());
        this.errorCode = UserErrorCode.USER_NOT_FOUND;
    }
}
