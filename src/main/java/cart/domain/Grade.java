package cart.domain;

public enum Grade {

    NORMAL(0.0),
    SILVER(0.05),
    GOLD(0.1),
    PLATINUM(0.15),
    DIAMOND(0.2),
    ;

    private final double discountRate;

    Grade(final double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
