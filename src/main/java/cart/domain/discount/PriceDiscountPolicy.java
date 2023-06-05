package cart.domain.discount;

import static java.lang.Math.min;

public final class PriceDiscountPolicy implements DiscountPolicy {
    private static final String NAME = "priceDiscount";
    private static final double MAX_DISCOUNT_RATE = 0.1;
    private static final int UNIT_OF_PRICE_DISCOUNT = 10000;

    @Override
    public int calculateDiscountAmount(final int price) {
        final double rate = min((double) (price / UNIT_OF_PRICE_DISCOUNT) / 100, MAX_DISCOUNT_RATE);
        return (int) (price * rate);
    }

    public double getRate(final int price) {
        return min((double) (price / UNIT_OF_PRICE_DISCOUNT) / 100, MAX_DISCOUNT_RATE);
    }

    public String getName() {
        return NAME;
    }
}
