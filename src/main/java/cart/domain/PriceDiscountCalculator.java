package cart.domain;

public class PriceDiscountCalculator {
    private static final String POLICY = "priceDiscount";
    private static final int MAX_DISCOUNT_MONEY = 100_000;
    private static final double MAX_DISCOUNT_RATE = 0.1;
    private static final int DISCOUNT_UNIT = 10_000;
    private static final int PLUS_VALUE_FOR_CALCULATE_DISCOUNT = 1;
    private static final double DISCOUNT_RATE_UNIT = 0.01;

    private final int price;

    public PriceDiscountCalculator(final int price) {
        this.price = price;
    }

    public double discountRate() {
        if (price > MAX_DISCOUNT_MONEY) {
            return MAX_DISCOUNT_RATE;
        }
        return (price / DISCOUNT_UNIT + PLUS_VALUE_FOR_CALCULATE_DISCOUNT) * DISCOUNT_RATE_UNIT;
    }

    public int getDiscountedPrice() {
        return (int) (price * discountRate());
    }

    public String getPolicyName(){
        return POLICY;
    }
}
