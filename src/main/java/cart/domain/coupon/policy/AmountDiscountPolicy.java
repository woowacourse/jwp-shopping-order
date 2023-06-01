package cart.domain.coupon.policy;

import cart.domain.cartitem.CartItems;
import cart.exception.StoreException;

public class AmountDiscountPolicy implements DiscountPolicy {

    @Override
    public void validateValue(final int discountAmount, final int minOrderPrice) {
        if (discountAmount <= 0) {
            throw new StoreException("할인 금액은 0원보다 커야합니다.");
        }
        if (minOrderPrice < discountAmount) {
            throw new StoreException("최소 주문 금액은 할인 금액보다 크거나 같아야합니다.");
        }
    }

    @Override
    public int calculateDiscountPrice(final int discountAmount, final CartItems cartItems) {
        return Math.min(discountAmount, cartItems.getTotalPrice());
    }
}
