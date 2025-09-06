package kr.hhplus.be.server.order.presentation.dto;

public class OrderResponse {
    public record OrderDto(
            String message
    ) {
        public static OrderDto of(String message) {
            return new OrderDto(message);
        }
    }
    public record PaymentDto(
            String message
    ) {
        public static PaymentDto of(String message) {
            return new PaymentDto(message);
        }
    }
}
