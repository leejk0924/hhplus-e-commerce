package kr.hhplus.be.server.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String code,
        String message
) {}