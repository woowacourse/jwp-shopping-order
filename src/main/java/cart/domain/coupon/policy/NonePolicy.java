package cart.domain.coupon.policy;

import cart.domain.cart.CartItems;

public class NonePolicy implements DiscountPolicy {

    @Override
    public void validateValue(final int value, final int minOrderPrice) {
    }

    @Override
    public int calculateDiscountPrice(final int value, final CartItems cartItems) {
        return 0;
    }
}
