package cart.domain.discount;

import static java.lang.Math.min;

public final class PriceDiscountPolicy implements DiscountPolicy {

    private static final int MAX_DISCOUNT_RATE = 10;

    @Override
    public int calculateDiscountAmount(final int price) {
        final int rate = min(price / 10000, MAX_DISCOUNT_RATE);
        return (int) (price * 0.01 * rate);
    }
}
