package cart.domain.coupon;

import cart.domain.coupon.policy.AmountDiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.PercentDiscountPolicy;

public enum CouponType {
    PERCENT(new PercentDiscountPolicy()),
    AMOUNT(new AmountDiscountPolicy()),
    ;

    private final DiscountPolicy discountPolicy;

    CouponType(final DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
