package kr.hhplus.be.server.order.infrastructure.adapter;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.order.application.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.exception.OrderErrorCode;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderEntityRepository;
import kr.hhplus.be.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderEntityRepository orderEntityRepository;
    @Override
    public Order saveOrder(Order order) {
        return orderEntityRepository.save(order);
    }
    @Override
    public Order findOrderById(Long orderId) {
        return orderEntityRepository.findById(orderId)
                .orElseThrow(() -> new RestApiException(OrderErrorCode.ORDER_NOT_FOUND));
    }
    @Override
    public UserCoupon findUserCouponByUserInfo(Long userCouponId, Long userId) {
        return orderEntityRepository.findUserCouponById(userCouponId, userId)
                .orElseThrow(() -> new RestApiException(OrderErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public User findUserById(Long userId) {
        return orderEntityRepository.findUserById(userId)
                .orElseThrow(() -> new RestApiException(OrderErrorCode.USER_NOT_FOUND));
    }
}
