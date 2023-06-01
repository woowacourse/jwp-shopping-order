package cart.domain;

public enum Grade {

    GOLD(0.05),
    SILVER(0.03),
    BRONZE(0.01),
    ;

    private final double discountRate;

    Grade(final double discountRate) {
        this.discountRate = discountRate;
    }

    public int calculateGradeDiscountPrice(final int price) {
        return (int) (price * discountRate);
    }

    public String getName() {
        return name();
    }

    public double getDiscountRate() {
        return discountRate;
    }

}
