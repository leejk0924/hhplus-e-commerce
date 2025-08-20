package kr.hhplus.be.server.order.application.dto;

import kr.hhplus.be.server.order.domain.entity.OrderItem;

public record OrderItemEventDto(Long productId, int quantity) {
    public static OrderItemEventDto from(OrderItem orderItem) {
        return new OrderItemEventDto(orderItem.getProduct().getId(), orderItem.getQuantity());
    }
}
