package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    public List<Product> searchProducts(List<Long> productIds) {
        return orderItemRepository.findProductsByIds(productIds);
    }
    public boolean deductProducts(List<Long> productIds) {
        try {
            List<OrderItem> orderItems = orderItemRepository.findAllOrderItems(productIds);
            orderItems.forEach(OrderItem::subtractProductStockQuantity);
            return true;
        }catch (RestApiException e) {
            throw e;
        }
    }
}
