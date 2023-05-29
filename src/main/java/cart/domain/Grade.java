package cart.domain;

public enum Grade {

    GOLD(5),
    SILVER(3),
    BRONZE(1),
    ;

    private final int discountRate;

    Grade(final int discountRate) {
        this.discountRate = discountRate;
    }

    public String getGrade() {
        return name().toLowerCase();
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
