package kr.hhplus.be.server.order.application.repository;

import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;

import java.util.List;

public interface OrderItemRepository {
    List<Product> findProductsByIds(List<Long> orderItemIds);
    List<OrderItem> findAllOrderItems(List<Long> orderItemIds);
}
