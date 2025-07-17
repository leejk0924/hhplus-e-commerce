package kr.hhplus.be.server.domain.dto;

import lombok.Getter;

public class OrderResponse {
    @Getter
    public static class ProcessOrderDto {
        String message;

        public ProcessOrderDto(String message) {
            this.message = message;
        }
    }
    @Getter
    public static class ProcessPaymentDto {
        String message;

        public ProcessPaymentDto(String message) {
            this.message = message;
        }
    }
}
