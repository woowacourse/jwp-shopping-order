package cart.domain.discount.price;

import org.springframework.stereotype.Component;

@Component
public class BasicPriceDiscountPolicy implements PriceDiscountPolicy {

    private static final int STANDARD_PRICE = 10_000;
    private static final int MAX_PRICE = 100_000;
    private static final double MAXIMUM_DISCOUNT_RATE = 0.1;
    private static final double MINIMUM_DISCOUNT_RATE = 0.01;

    @Override
    public Integer calculateDiscountPrice(Integer price) {
        if (price >= MAX_PRICE) {
            return (int) (MAX_PRICE * MAXIMUM_DISCOUNT_RATE);
        }
        final double discountRate = Math.floor((double) price / STANDARD_PRICE) * MINIMUM_DISCOUNT_RATE + MINIMUM_DISCOUNT_RATE;
        return (int) (price * discountRate);
    }
}
