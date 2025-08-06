package kr.hhplus.be.server.order.infrastructure.persistence.jpa;

import kr.hhplus.be.server.order.domain.entity.OrderItem;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemEntityRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = """
                SELECT p.* FROM PRODUCTS p
                WHERE p.id IN (:productIds)
            """, nativeQuery = true)
    List<Product> findProductsByProductIds(@Param("productIds") List<Long> productIds);
}
