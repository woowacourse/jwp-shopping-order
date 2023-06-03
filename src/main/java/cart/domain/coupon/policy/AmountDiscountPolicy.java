package cart.domain.coupon.policy;

import static cart.exception.badrequest.BadRequestErrorType.DISCOUNT_AMOUNT_INVALID;
import static cart.exception.badrequest.BadRequestErrorType.MIN_ORDER_PRICE_INVALID;

import cart.domain.cart.CartItems;
import cart.exception.badrequest.BadRequestException;

public class AmountDiscountPolicy implements DiscountPolicy {

    @Override
    public void validateValue(final int discountAmount, final int minOrderPrice) {
        if (discountAmount <= 0) {
            throw new BadRequestException(DISCOUNT_AMOUNT_INVALID);
        }
        if (minOrderPrice < discountAmount) {
            throw new BadRequestException(MIN_ORDER_PRICE_INVALID);
        }
    }

    @Override
    public int calculateDiscountPrice(final int discountAmount, final CartItems cartItems) {
        return Math.min(discountAmount, cartItems.calculateTotalProductPrice());
    }
}
