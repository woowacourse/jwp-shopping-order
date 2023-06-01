package cart.domain.coupon.policy;

import cart.domain.cart.CartItems;
import cart.exception.StoreException;

public class PercentDiscountPolicy implements DiscountPolicy {

    private static final int PERCENT_BASE = 100;

    @Override
    public void validateValue(final int percent, final int minOrderPrice) {
        if (percent <= 0 || PERCENT_BASE < percent) {
            throw new StoreException("잘못된 할인율입니다.");
        }
    }

    @Override
    public int calculateDiscountPrice(final int percent, final CartItems cartItems) {
        int totalPrice = cartItems.getTotalProductPrice();
        return totalPrice * percent / PERCENT_BASE;
    }
}
