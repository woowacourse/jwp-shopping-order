package cart.fixture;

import cart.domain.Coupon;
import cart.domain.policy.DiscountPolicyResolver;
import cart.domain.policy.PercentDiscountPolicy;
import cart.domain.policy.PriceDiscountPolicy;

public class CouponFixture {
    public static final Coupon COUPON1_Percent_10 = new Coupon(
            "쿠폰1",
            DiscountPolicyResolver.of(
                    PercentDiscountPolicy.NAME,
                    10));
    public static final Coupon COUPON2_Price_1000 = new Coupon(
            "쿠폰2",
            DiscountPolicyResolver.of(
                    PriceDiscountPolicy.NAME,
                    1000));
}
