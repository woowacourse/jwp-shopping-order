package cart.domain.price.discount.price;

import cart.domain.Member;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.DiscountPolicy;
import cart.domain.price.discount.grade.Grade;

public class BasicPriceDiscountPolicy implements DiscountPolicy {
    private static final String NAME = "priceDiscount";
    private static final int STANDARD_PRICE = 10_000;
    private static final int MAX_PRICE = 100_000;
    private static final double MAXIMUM_DISCOUNT_RATE = 0.1;
    private static final double MINIMUM_DISCOUNT_RATE = 0.01;

    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        final Double discountRate = calculateDiscountRate(price);
        return (int) (price * discountRate);
    }

    @Override
    public DiscountInformation computeDiscountInformation(Integer price, Grade grade) {
        final Double discountRate = calculateDiscountRate(price);
        final Integer discountPrice = (int) (price * discountRate);
        return new DiscountInformation(NAME, discountRate, discountPrice);
    }

    private Double calculateDiscountRate(Integer price) {
        if (price >= MAX_PRICE) {
            return MAXIMUM_DISCOUNT_RATE;
        }
        return Math.floor((double) price / STANDARD_PRICE) * MINIMUM_DISCOUNT_RATE + MINIMUM_DISCOUNT_RATE;
    }
}
