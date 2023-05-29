package cart.domain.coupon;

import cart.exception.GlobalException;

public class DiscountRate {
    private static final int MIN_DISCOUNT_RATE = 5;
    private static final int MAX_DISCOUNT_RATE = 90;

    private final int discountRate;

    public DiscountRate(int discountRate) {
        validate(discountRate);
        this.discountRate = discountRate;
    }

    private void validate(int discountRate) {
        if (discountRate < MIN_DISCOUNT_RATE || discountRate > MAX_DISCOUNT_RATE) {
            throw new GlobalException("쿠폰 할인율은 5~90% 사이여야 합니다.");
        }
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
