package cart.service;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public abstract class CouponFixture {

    protected long couponId;
    protected Coupons coupons;
    protected Coupon unUsedCoupon;
    protected Coupon usedCoupon;

    @BeforeEach
    void setup() {
        Coupon coupon1 = new Coupon(1L, 1L, "1000원 할인 쿠폰", "1000원이 할인 됩니다.", 1000, false);
        Coupon coupon2 = new Coupon(2L, 2L, "2000원 할인 쿠폰", "2000원이 할인 됩니다.", 2000, false);
        Coupon coupon3 = new Coupon(3L, 3L, "3000원 할인 쿠폰", "3000원이 할인 됩니다.", 3000, false);
        coupons = new Coupons(List.of(coupon1, coupon2, coupon3));

        couponId = 1L;
        usedCoupon = new Coupon(couponId, 1L, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, true);
        unUsedCoupon = new Coupon(couponId, 1L, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, false);
    }
}
