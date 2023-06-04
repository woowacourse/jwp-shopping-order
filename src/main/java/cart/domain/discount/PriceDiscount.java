package cart.domain.discount;

public class PriceDiscount implements Discount {
    private static final String POLICY_NAME = "priceDiscount";
    private static final int MAX_DISCOUNT_MONEY = 100_000;
    private static final double MAX_DISCOUNT_RATE = 0.1;
    private static final int DISCOUNT_UNIT = 10_000;
    private static final int PLUS_VALUE_FOR_CALCULATE_DISCOUNT = 1;
    private static final double DISCOUNT_RATE_UNIT = 0.01;

    private final int price;

    public PriceDiscount(final int price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return POLICY_NAME;
    }

    @Override
    public double getDiscountRate() {
        if (price >= MAX_DISCOUNT_MONEY) {
            return MAX_DISCOUNT_RATE;
        }
        return (price / DISCOUNT_UNIT + PLUS_VALUE_FOR_CALCULATE_DISCOUNT) * DISCOUNT_RATE_UNIT;
    }

    @Override
    public int getDiscountedPrice() {
        return (int) (price * getDiscountRate());
    }
}
