package cart.fixture;

import cart.domain.Coupon;
import cart.domain.CouponType;

public class CouponFixture {

    public static final Coupon 천원_할인_쿠폰 = new Coupon(1L, "1000원 할인 쿠폰", CouponType.PRICE, 1000);
    public static final Coupon 십프로_할인_쿠폰 = new Coupon(2L, "10% 할인 쿠폰", CouponType.RATE, 10);
}
