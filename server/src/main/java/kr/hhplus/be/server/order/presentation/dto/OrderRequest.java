package kr.hhplus.be.server.order.presentation.dto;

import java.util.List;

public class OrderRequest {
    public record OrderDto(
            Long userId,
            List<OrderItemDto> orderItems
    ) {

    }
    public record OrderItemDto(
            Long productId,
            Integer quantity
    ) {
    }
    public record ProcessPaymentDto(
            Long userId,
            Long orderId,
            Long userCouponId
    ) {
        public static ProcessPaymentDto of(Long userId, Long orderId, Long userCouponId) {
            return new ProcessPaymentDto(userId, orderId, userCouponId);
        }
    }
}
