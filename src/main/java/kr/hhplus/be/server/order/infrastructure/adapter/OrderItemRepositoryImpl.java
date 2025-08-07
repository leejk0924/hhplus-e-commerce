package kr.hhplus.be.server.order.infrastructure.adapter;

import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderItemEntityRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final OrderItemEntityRepository orderItemEntityRepository;
    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return orderItemEntityRepository.findProductsByProductIds(productIds);
    }

    @Override
    public List<OrderItem> findAllOrderItems(List<Long> orderItemIds) {
        return orderItemEntityRepository.findAllByIdInForUpdate(orderItemIds);
    }
}
