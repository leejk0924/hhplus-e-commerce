package kr.hhplus.be.server.coupon.infrastructure.persistence.jpa;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponEntityRepository extends JpaRepository<UserCoupon, Long> {
    @Query(value = """
            SELECT CASE WHEN COUNT(uc) > 0
                             THEN TRUE
                             ELSE FALSE
                        END
                   FROM USERS_COUPON uc
                  WHERE uc.userId      = :userId
                    AND uc.couponId.id = :couponId
            """)
    boolean existsByUserAndCoupon(Long userId, Long couponId);
}
