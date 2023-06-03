package cart.domain.coupon;

import cart.domain.cart.CartItems;
import cart.domain.coupon.policy.AmountDiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.NonePolicy;
import cart.domain.coupon.policy.PercentDiscountPolicy;

public enum CouponType {
    NONE(new NonePolicy()),
    FIXED_PERCENTAGE(new PercentDiscountPolicy()),
    FIXED_AMOUNT(new AmountDiscountPolicy()),
    ;

    private final DiscountPolicy discountPolicy;

    CouponType(final DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void validateValue(final int value, final int minOrderPrice) {
        discountPolicy.validateValue(value, minOrderPrice);
    }

    public int calculateDiscountPrice(final int value, final CartItems cartItems) {
        return discountPolicy.calculateDiscountPrice(value, cartItems);
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
