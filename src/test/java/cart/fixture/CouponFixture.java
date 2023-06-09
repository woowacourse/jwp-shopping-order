package cart.fixture;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.ProductCoupon;
import cart.domain.disount.DiscountPolicyResolver;
import cart.domain.disount.PercentDiscountPolicy;
import cart.domain.disount.PriceDiscountPolicy;

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
