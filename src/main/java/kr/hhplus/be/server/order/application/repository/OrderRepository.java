package kr.hhplus.be.server.order.application.repository;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.user.domain.entity.User;

public interface OrderRepository {
    Order saveOrder(Order order);
    Order findOrderById(Long orderId);
    UserCoupon findUserCouponByUserInfo(Long userCouponId, Long userId);
    User findUserById(Long userId);
}
