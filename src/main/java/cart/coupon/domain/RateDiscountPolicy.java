package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.INVALID_DISCOUNT_RATE;

import cart.coupon.exception.CouponException;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal decimalPrice = BigDecimal.valueOf(price);
        BigDecimal discount = BigDecimal.valueOf(discountRate).divide(BigDecimal.valueOf(100));
        BigDecimal discountAmount = decimalPrice.multiply(discount).setScale(0, RoundingMode.FLOOR);
        BigDecimal finalPrice = decimalPrice.subtract(discountAmount);
        return finalPrice.intValue();
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
