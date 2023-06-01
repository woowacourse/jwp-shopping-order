package cart.fixture;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.ProductCoupon;
import cart.domain.policy.DiscountPolicyResolver;
import cart.domain.policy.PercentDiscountPolicy;
import cart.domain.policy.PriceDiscountPolicy;

public class CouponFixture {
    public static final Coupon PRODUCT_COUPON1_Percent_10 = new ProductCoupon(
            "쿠폰1",
            DiscountPolicyResolver.of(
                    PercentDiscountPolicy.NAME,
                    10));
    public static final Coupon PRODUCT_COUPON2_Price_1000 = new ProductCoupon(
            "쿠폰2",
            DiscountPolicyResolver.of(
                    PriceDiscountPolicy.NAME,
                    1000));
}
