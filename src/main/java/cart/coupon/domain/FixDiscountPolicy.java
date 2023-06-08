package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.INVALID_DISCOUNT_AMOUNT;

import cart.coupon.exception.CouponException;

public class FixDiscountPolicy implements DiscountPolicy {

    private final int discountAmount;

    public FixDiscountPolicy(int discountAmount) {
        validateDiscountAmount(discountAmount);
        this.discountAmount = discountAmount;
    }

    private void validateDiscountAmount(int discountAmount) {
        if (discountAmount <= 0) {
            throw new CouponException(INVALID_DISCOUNT_AMOUNT);
        }
    }

    @Override
    public int calculatePrice(int price) {
        if (price < discountAmount) {
            return 0;
        }
        return price - discountAmount;
    }

    @Override
    public int getValue() {
        return discountAmount;
    }

    @Override
    public DiscountType getDiscountType() {
        return DiscountType.FIX;
    }
}
