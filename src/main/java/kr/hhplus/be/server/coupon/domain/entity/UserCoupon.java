package kr.hhplus.be.server.coupon.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "USERS_COUPON")
@Entity(name = "USERS_COUPON")
public class UserCoupon extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_ID", nullable = false)
    private Coupon couponId;
    @Column(name = "COUPON_STATUS", length = 20)
    private String couponStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "REDEEMED_AT")
    private LocalDateTime redeemedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    public UserCoupon (
            Long userId,
            Coupon couponId,
            String couponStatus,
            LocalDateTime redeemedAt,
            LocalDateTime expiredAt
    ) {
        if (userId == null) {
            throw new RestApiException(CouponErrorCode.INVALID_USER);
        }
        if  (couponId == null || couponId.getId() == null) {
            throw new RestApiException(CouponErrorCode.INVALID_COUPON);
        }
        this.userId = userId;
        this.couponId = couponId;
        this.couponStatus = couponStatus;
        this.redeemedAt = redeemedAt;
        this.expiredAt = expiredAt;
    }
    public static UserCoupon of(
            Long userId,
            Coupon couponId,
            String couponStatus,
            LocalDateTime redeemedAt,
            LocalDateTime issuedAt
    ) {
        return new UserCoupon(userId, couponId, couponStatus, redeemedAt, issuedAt);
    }
}
