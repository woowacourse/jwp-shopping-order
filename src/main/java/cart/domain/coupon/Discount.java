package cart.domain.coupon;

public class Discount {

    private final Strategy strategy;
    private final int amount;

    public Discount(final Strategy strategy, final int amount) {
        this.strategy = strategy;
        this.amount = amount;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public int getAmount() {
        return amount;
    }
}
