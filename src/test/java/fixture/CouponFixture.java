package fixture;

import cart.domain.Coupon;

public class CouponFixture {

    public static final Coupon COUPON_1_NOT_NULL_PRICE = new Coupon(1L, "정액 할인 쿠폰", 0.0, 5000);
    public static final Coupon COUPON_2_NOT_NULL_RATE = new Coupon(2L, "할인율 쿠폰", 10.0, 0);
    public static final Coupon COUPON_3_NULL = null;

}
