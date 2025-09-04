package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.order.application.dto.OrderItemCommand;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        return orderRepository.saveOrder(order);
    }

    public Order createOrder(List<Product> products, List<OrderItemCommand.ProductCommand> orderItemCommands, Long userId) {
        Order order = Order.of(userId, "주문상태");
        Map<Long, Product> productmap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> orderItems = orderItemCommands.stream().map(cmd -> {
            Product product = productmap.get(cmd.productId());
            return OrderItem.of(order, product, cmd.quantity(), product.getPrice());
        }).toList();

        order.addOrderItems(orderItems);
        return order;
    }

    public Order searchOrder(Long orderId) {
        return orderRepository.findOrderById(orderId);
    }

    public UserCoupon searchUserCoupon(Long userCouponId, Long userId) {
        return orderRepository.findUserCouponByUserInfo(userCouponId, userId);
    }

    private User searchUser(Long userId) {
        return orderRepository.findUserById(userId);
    }

    @Transactional
    public boolean withdrawPayment(PayCommand payCommand, Order order) {
        User user = searchUser(payCommand.userId());
        user.withdrawPoint(order.getPaymentAmount());
        return true;
    }

    public void savePayment(Order order) {
        order.completePayment();
        orderRepository.saveOrder(order);
    }
}
