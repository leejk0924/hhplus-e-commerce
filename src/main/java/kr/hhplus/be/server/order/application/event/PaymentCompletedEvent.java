package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.order.application.dto.OrderItemEventDto;

import java.util.List;

public record PaymentCompletedEvent (
        Long orderId,
        List<OrderItemEventDto> orderItems
){}
