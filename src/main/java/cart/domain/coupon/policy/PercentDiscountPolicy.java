package cart.domain.coupon.policy;

import static cart.exception.badrequest.BadRequestErrorType.DISCOUNT_PERCENT_INVALID;

import cart.domain.cart.CartItems;
import cart.exception.badrequest.BadRequestException;

public class PercentDiscountPolicy implements DiscountPolicy {

    private static final int PERCENT_BASE = 100;

    @Override
    public void validateValue(final int percent, final int minOrderPrice) {
        if (percent <= 0 || PERCENT_BASE < percent) {
            throw new BadRequestException(DISCOUNT_PERCENT_INVALID);
        }
    }

    @Override
    public int calculateDiscountPrice(final int percent, final CartItems cartItems) {
        int totalPrice = cartItems.calculateTotalProductPrice();
        return totalPrice * percent / PERCENT_BASE;
    }
}
