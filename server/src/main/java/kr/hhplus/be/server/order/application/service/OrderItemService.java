package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    public List<Product> searchProducts(List<Long> productIds) {
        return orderItemRepository.findProductsByIds(productIds);
    }
    @Transactional
    public List<OrderItem> deductProducts(List<Long> orderItemIds) {
        try {
            List<OrderItem> orderItems = orderItemRepository.findAllOrderItems(orderItemIds);
            orderItems.forEach(OrderItem::subtractProductStockQuantity);
            return orderItems;
        }catch (RestApiException e) {
            throw e;
        }
    }
    public List<Long> searchProductIdsByOrderItemIds(List<Long> orderItemIds) {
        return orderItemRepository.findProductIdsByOrderItemIds(orderItemIds);
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> getProductQuantities(List<Long> orderItemIds) {
        List<OrderItem> orderItems = orderItemRepository.findAllOrderItems(orderItemIds);
        return orderItems.stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(),
                OrderItem::getQuantity,
                Integer::sum));
    }
}
