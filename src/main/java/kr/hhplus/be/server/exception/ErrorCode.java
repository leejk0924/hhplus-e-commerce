package kr.hhplus.be.server.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus statusCode();
    String getMessage();
}