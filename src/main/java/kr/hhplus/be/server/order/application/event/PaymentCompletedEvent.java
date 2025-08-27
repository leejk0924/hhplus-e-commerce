package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.config.event.DispatchableEvent;
import kr.hhplus.be.server.order.application.dto.OrderItemEventDto;
import lombok.Getter;

import java.util.List;

public class PaymentCompletedEvent extends DispatchableEvent {
    @Getter
    private final List<OrderItemEventDto> orderItems;

    public PaymentCompletedEvent(Long orderId, List<OrderItemEventDto> orderItems, boolean async) {
        super(orderId, async);
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return (Long) getSource();
    }
}
