package kr.hhplus.be.server.product.infrastructure.persistence.jpa;

import kr.hhplus.be.server.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductEntityRepository extends JpaRepository<Product, Long> {
    @Query(value = """
    SELECT p.*
      FROM order_items oi
      JOIN products    p
        ON oi.product_id = p.id
     WHERE oi.purchased_at >= :startDate
     GROUP BY oi.product_id
     ORDER BY SUM(oi.quantity) DESC
     LIMIT 5
    """, nativeQuery = true)
    List<Product> findPopularProducts(@Param("startDate")LocalDate startDate);
}
