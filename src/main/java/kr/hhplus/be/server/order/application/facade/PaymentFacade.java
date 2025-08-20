package kr.hhplus.be.server.order.application.facade;

import kr.hhplus.be.server.common.redis.lock.DistributedLock;
import kr.hhplus.be.server.common.redis.lock.key.OrderItemLockKeyGenerator;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.order.application.dto.OrderItemEventDto;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.event.PaymentCompletedEvent;
import kr.hhplus.be.server.order.application.service.OrderItemService;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ApplicationEventPublisher eventPublisher;

    @DistributedLock(keyGenerator = OrderItemLockKeyGenerator.class)
    public void payProcess(PayCommand payCommand) {
        Order order = orderService.searchOrder(payCommand.orderId());
        List<OrderItem> orderItems = orderItemService.deductProducts(order.toOrderItemIds());
        if (payCommand.userCouponId() != null) {
            UserCoupon userCoupon = orderService.searchUserCoupon(payCommand.userCouponId(), payCommand.userId());
            userCoupon.validateUsable();
            order.applyCoupon(userCoupon);
        }
        orderService.withdrawPayment(payCommand, order);
        orderService.savePayment(order);
        eventPublisher.publishEvent(new PaymentCompletedEvent(order.getId(), orderItems.stream()
                .map(OrderItemEventDto::from)
                .toList()));
    }
}
