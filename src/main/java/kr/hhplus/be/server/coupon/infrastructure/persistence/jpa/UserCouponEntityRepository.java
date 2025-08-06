package kr.hhplus.be.server.coupon.infrastructure.persistence.jpa;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponEntityRepository extends JpaRepository<UserCoupon, Long> {
    @Query(value = """
           SELECT EXISTS (
             SELECT 1
               FROM users_coupon uc
              WHERE uc.user_id   = :userId
                AND uc.coupon_id = :couponId
           )
           """, nativeQuery = true)
    boolean existsByUserAndCoupon(Long userId, Long couponId);
}
