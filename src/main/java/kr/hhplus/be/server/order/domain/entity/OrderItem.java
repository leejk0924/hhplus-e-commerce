package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
@Entity(name = "order_items")
public class OrderItem extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product productId;
    @Column(name = "QUANTITY", nullable = false, columnDefinition = "INT UNSIGNED")
    private int quantity;
    @Column(name = "UNIT_PRICE", nullable = false, columnDefinition = "INT UNSIGNED")
    private int unitPrice;
    @CreatedDate
    @Column(name = "PURCHASED_AT", updatable = false)
    private LocalDate purchasedAt;

    public OrderItem(Product productId, int quantity, int unitPrice, LocalDate purchasedAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.purchasedAt = purchasedAt;
    }
}
