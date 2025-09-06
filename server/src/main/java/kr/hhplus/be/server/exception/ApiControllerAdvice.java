package kr.hhplus.be.server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<ErrorResponse> handleRestApiException(RestApiException e) {
        return ResponseEntity
                .status(e.getErrorCode().statusCode())
                .body(ErrorResponse.of(e.getErrorCode()));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(CommonErrorCode.INVALID_PARAMETER));

    }
}