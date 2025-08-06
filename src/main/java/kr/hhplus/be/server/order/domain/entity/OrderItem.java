package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.order.exception.OrderErrorCode;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "ORDER_ITEMS")
@Entity(name = "ORDER_ITEMS")
public class OrderItem extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
    @Column(name = "QUANTITY", nullable = false, columnDefinition = "INT UNSIGNED")
    private int quantity;
    @Column(name = "UNIT_PRICE", nullable = false, columnDefinition = "INT UNSIGNED")
    private int unitPrice;
    @CreatedDate
    @Column(name = "PURCHASED_AT", updatable = false)
    private LocalDate purchasedAt;

    public OrderItem(Order order, Product product, int quantity, int unitPrice) {
        if (quantity <= 0) {
            throw new RestApiException(OrderErrorCode.INVALID_QUANTITY);
        }
        if (product.getStockQuantity() < quantity) {
            throw new RestApiException(OrderErrorCode.OUT_OF_STOCK);
        }
        if (unitPrice < 0) {
            throw new RestApiException(OrderErrorCode.INVALID_PRICE);
        }
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.purchasedAt = null;
    }
    public void subtractProductStockQuantity() {
        int stockQuantity = product.getStockQuantity();
        if (quantity > stockQuantity) {
            throw new RestApiException(OrderErrorCode.OUT_OF_STOCK);
        }
        product.decreaseStockQuantity(quantity);
    }
    public static OrderItem of(Order order, Product product, int quantity, int unitPrice) {
        return new OrderItem(order, product, quantity, unitPrice);
    }
}
