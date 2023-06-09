package cart.domain.fixture;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.AmountDiscountPolicy;
import cart.domain.discountpolicy.RateDiscountPolicy;
import cart.dto.request.CouponRequest;

public class CouponFixture {

    public static final Coupon RATE_10_COUPON = new Coupon("10퍼센트 할인 쿠폰", new RateDiscountPolicy(), 0.9);
    public static final Coupon AMOUNT_1000_COUPON = new Coupon("1000원 할인 쿠폰", new AmountDiscountPolicy(), 1000);

    public static final CouponRequest RATE_10_COUPON_REQUEST = new CouponRequest("10퍼센트 할인 쿠폰", "percent", 10);
    public static final CouponRequest AMOUNT_1000_COUPON_REQUEST = new CouponRequest("1000원 할인 쿠폰", "amount", 1000);
}
