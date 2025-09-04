package kr.hhplus.be.server.order.application.dto;

import kr.hhplus.be.server.order.presentation.dto.OrderRequest;

public record PayCommand(
        Long userId,
        Long orderId,
        Long userCouponId
) {
    public static PayCommand toPayCommand(OrderRequest.ProcessPaymentDto processInfo) {
        return new PayCommand(processInfo.userId(), processInfo.orderId(), processInfo.userCouponId());
    }
}
