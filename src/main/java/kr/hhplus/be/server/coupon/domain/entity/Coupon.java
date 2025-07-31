package kr.hhplus.be.server.coupon.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "coupon")
@Entity(name = "coupon")
public class Coupon extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "COUPON_NAME", length = 20, nullable = false)
    private String couponName;
    @Comment("쿠폰 할인 금액 / 할인율")
    @Column(name = "DISCOUNT_TYPE", length = 20, nullable = false)
    private String discountType;
    @Column(name = "DISCOUNT_RATE", nullable = false)
    private int discountRate;
    @Comment("쿠폰 재고")
    @Column(name = "COUPON_INVENTORY",  nullable = false)
    private int couponInventory;

    public Coupon(
            Long id,
            String couponName,
            String discountType,
            int discountRate,
            int couponInventory) {
        this.id = id;
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.couponInventory = couponInventory;
    }
    public static Coupon of(
            Long id,
            String couponName,
            String discountType,
            int discountRate,
            int couponInventory) {
        return new Coupon(id, couponName, discountType, discountRate, couponInventory);
    }
    public void issue() {
        if (couponInventory <= 0) {
            throw new RestApiException(CouponErrorCode.COUPON_OUT_OF_STOCK);
        }
        this.couponInventory--;
    }
}
