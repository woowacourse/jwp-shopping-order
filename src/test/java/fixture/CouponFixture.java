package fixture;

import cart.domain.Coupon;

public class CouponFixture {

    public static final Coupon 정액_할인_쿠폰 = new Coupon(1L, "정액 할인 쿠폰", 0.0, 5000);
    public static final Coupon 할인율_쿠폰 = new Coupon(2L, "할인율 쿠폰", 10.0, 0);
    public static final Coupon 빈_쿠폰 = null;

}
