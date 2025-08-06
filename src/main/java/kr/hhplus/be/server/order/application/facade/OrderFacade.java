package kr.hhplus.be.server.order.application.facade;

import kr.hhplus.be.server.order.application.dto.OrderItemCommand;
import kr.hhplus.be.server.order.application.service.OrderItemService;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    @Transactional
    public void createOrder(OrderItemCommand orderDtos, Long userId) {
        List<Product> products = orderItemService.searchProducts(orderDtos.toProductIds());
        Order order = orderService.createOrder(products, orderDtos.productCommands(), userId);
        orderService.saveOrder(order);
    }
}
