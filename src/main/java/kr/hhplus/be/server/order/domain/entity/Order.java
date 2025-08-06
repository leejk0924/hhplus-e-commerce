package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "ORDERS")
@Entity(name = "ORDERS ")
public class Order extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "USER_ID")
    private Long user;
    @Column(name= "USER_COUPON_ID")
    private Long userCouponId;
    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();
    @Column(name = "PRODUCT_TOTAL_AMOUNT", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer productTotalAmount;
    @Column(name = "TOTAL_DISCOUNT", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer totalDiscount;
    @Column(name = "PAYMENT_AMOUNT", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer paymentAmount;
    @Column(name = "ORDER_STATUS", length = 20)
    private String orderStatus;
    @CreatedDate
    @Column(name = "PURCHASED_AT", updatable = false)
    private LocalDate purchased_at;

    public Order(
            Long userId,
            String orderStatus
    ) {
        this.user = userId;
        this.userCouponId = null;
        this.productTotalAmount = 0;
        this.totalDiscount = 0;
        this.paymentAmount = 0;
        this.orderStatus = orderStatus;
        this.purchased_at = null;
    }
    public static Order of(
            Long userId,
            String orderStatus
    ) {
        return new Order(
                userId,
                orderStatus
        );
    }
    private int getitmesTotalAmount() {
        return orderItems.stream().mapToInt(it -> it.getQuantity() * it.getUnitPrice()).sum();
    }
    public void addOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        int totalItemsPrice = getitmesTotalAmount();
        this.productTotalAmount = totalItemsPrice;
        this.paymentAmount = totalItemsPrice;
    }
    public List<Long> toProductIds() {
        return this.orderItems.stream().map(i -> {
            return i.getId();
        }).toList();
    }
    public void applyCoupon(UserCoupon userCoupon) {
        this.userCouponId = userCoupon.getId();
        if ("AMOUNT".equals(userCoupon.getCouponId().getDiscountType())) {
            this.totalDiscount = userCoupon.getCouponId().getDiscountRate();
        }
        if ("PERCENT".equals(userCoupon.getCouponId().getDiscountType())) {
            Integer discountRate = userCoupon.getCouponId().getDiscountRate();
            this.totalDiscount = (productTotalAmount / 100) * discountRate;
        }
        this.paymentAmount = productTotalAmount - totalDiscount;
    }
    public void completePayment() {
        this.orderStatus = "결제완료";
    }
}
