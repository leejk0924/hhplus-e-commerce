package kr.hhplus.be.server.order.infrastructure.persistence.jpa;

import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderEntityRepository extends JpaRepository<Order, Long> {
    @Query(value = """
                SELECT uc.*
                    FROM USERS_COUPON uc
                WHERE ID = :userCouponId AND USER_ID = :userId
            """, nativeQuery = true)
    Optional<UserCoupon> findUserCouponById(@Param("userCouponId") Long userCouponId, @Param("userId") Long userId);

    @Query(value = """
    SELECT u FROM USERS u WHERE u.id = :userId
    """)
    Optional<User> findUserById(@Param("userId") Long userId);
}
