package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemEntityRepository extends JpaRepository<OrderItem, Long> {
}
