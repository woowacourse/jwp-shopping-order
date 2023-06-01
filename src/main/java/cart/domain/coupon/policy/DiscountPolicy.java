package cart.domain.coupon.policy;

import cart.domain.cartitem.CartItems;

public interface DiscountPolicy {

    void validateValue(final int value, final int minOrderPrice);

    int calculateDiscountPrice(final int value, final CartItems cartItems);
}
