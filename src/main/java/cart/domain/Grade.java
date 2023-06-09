package cart.domain;

public enum Grade {

    NORMAL(0.0, "일반"),
    SILVER(0.05, "실버"),
    GOLD(0.1, "골드"),
    PLATINUM(0.15, "플래티넘"),
    DIAMOND(0.2, "다이아몬드"),
    ;

    private final double discountRate;
    private final String korean;

    Grade(final double discountRate, final String korean) {
        this.discountRate = discountRate;
        this.korean = korean;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public String getKorean() {
        return korean;
    }
}
