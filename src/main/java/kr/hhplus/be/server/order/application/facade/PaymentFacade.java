package kr.hhplus.be.server.order.application.facade;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.service.OrderItemService;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    @Transactional
    public void payProcess(PayCommand payCommand) {
        Order order = orderService.searchOrder(payCommand.orderId());
        orderItemService.deductProducts(order.toProductIds());
        if (payCommand.userCouponId() != null) {
            UserCoupon userCoupon = orderService.searchUserCoupon(payCommand.userCouponId(), payCommand.userId());
            userCoupon.validateUsable();
            order.applyCoupon(userCoupon);
        }
        orderService.withdrawPayment(payCommand, order);
        orderService.savePayment(order);
    }
}
