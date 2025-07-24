package kr.hhplus.be.server.exception;

public interface ErrorCode {
    String name();
    Integer StatusCode();
    String getMessage();
}
