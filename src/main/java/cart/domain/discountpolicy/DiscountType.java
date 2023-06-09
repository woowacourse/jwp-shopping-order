package cart.domain.discountpolicy;

public enum DiscountType {
    RATE(1),
    AMOUNT(2);

    private final int priority;

    DiscountType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
