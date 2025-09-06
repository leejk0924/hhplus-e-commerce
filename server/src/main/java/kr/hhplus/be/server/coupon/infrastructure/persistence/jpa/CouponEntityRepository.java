package kr.hhplus.be.server.coupon.infrastructure.persistence.jpa;

import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponEntityRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT c FROM COUPON c WHERE c.id = :id")
    Optional<Coupon> findByIdForUpdate(@Param("id") Long id);
}
