package kr.hhplus.be.server.order.infrastructure.persistence.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemEntityRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = """
                SELECT p.* FROM PRODUCTS p
                WHERE p.id IN (:productIds)
            """, nativeQuery = true)
    List<Product> findProductsByProductIds(@Param("productIds") List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT oi FROM ORDER_ITEMS oi WHERE oi.id IN :orderItemIds
            """)
    List<OrderItem> findAllByIdInForUpdate(@Param("orderItemIds") List<Long> orderItemIds);
}
