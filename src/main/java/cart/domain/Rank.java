package cart.domain;

public enum Rank {

    NORMAL(0.0),
    SILVER(0.05),
    GOLD(0.1),
    PLATINUM(0.15),
    DIAMOND(0.2),
    ;

    private final double discountRate;

    Rank(final double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
