package kr.hhplus.be.server.stub;

import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import org.springframework.test.util.ReflectionTestUtils;

public class StubCoupon extends Coupon {
    public StubCoupon(Long id, String couponName, String discountType, int discountRate, int couponInventory) {
        super();
        ReflectionTestUtils.setField(this, "id", id);
        ReflectionTestUtils.setField(this, "couponName", couponName);
        ReflectionTestUtils.setField(this, "discountType", discountType);
        ReflectionTestUtils.setField(this, "discountRate", discountRate);
        ReflectionTestUtils.setField(this, "couponInventory", couponInventory);
    }
    public static StubCoupon of(Long id, String couponName, String discountType, int discountRate, int couponInventory) {
        return new StubCoupon(id, couponName, discountType, discountRate, couponInventory);
    }
}
