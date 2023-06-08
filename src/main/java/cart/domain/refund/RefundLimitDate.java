package cart.domain.refund;

public enum RefundLimitDate {
    FULL_REFUND(3),
    HALF_REFUND(7);

    private final int day;

    RefundLimitDate(final int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}
