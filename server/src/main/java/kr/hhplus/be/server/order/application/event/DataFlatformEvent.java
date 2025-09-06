package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.config.event.DispatchableEvent;
import kr.hhplus.be.server.order.domain.entity.Order;
import lombok.Getter;

public class DataFlatformEvent extends DispatchableEvent {
    @Getter
    private final Order order;
    public DataFlatformEvent(Long orderId ,Order order, boolean isAsync) {
        super(orderId, isAsync);
        this.order = order;
    }
    public Long getOrderId() {
        return (Long) getSource();
    }
}
