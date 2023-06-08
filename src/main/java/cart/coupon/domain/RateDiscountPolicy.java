package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.INVALID_DISCOUNT_RATE;

import cart.coupon.exception.CouponException;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public RateDiscountPolicy(int discountRate) {
        validateDiscountRate(discountRate);
        this.discountRate = discountRate;
    }

    private void validateDiscountRate(int discountRate) {
        if (!(0 < discountRate && discountRate <= 100)) {
            throw new CouponException(INVALID_DISCOUNT_RATE);
        }
    }

    @Override
    public int calculatePrice(int price) {
        return price - (int) (price * (discountRate / 100.0));
    }

    @Override
    public int getValue() {
        return discountRate;
    }

    @Override
    public DiscountType getDiscountType() {
        return DiscountType.RATE;
    }
}
