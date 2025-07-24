package kr.hhplus.be.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저 ID에 대한 회원정보가 없습니다."),
    INVALID_POINT(HttpStatus.BAD_REQUEST.value(), "보유 포인트가 유효하지 않습니다."),
    INVALID_CHARGE_POINT(HttpStatus.BAD_REQUEST.value(), "충전하는 포인트가 유효하지 않습니다."),
    EXCEEDED_POINT(HttpStatus.BAD_REQUEST.value(), "보유할 수 있는 최대 포인트는 1000만원 까지입니다.");
    private final int httpStatusCode;
    private final String message;
    @Override
    public Integer StatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
