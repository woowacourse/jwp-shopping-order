package cart.domain.coupon;

public enum DiscountType {
    PERCENTAGE("percentage"),
    DEDUCTION("deduction");

    private final String name;

    DiscountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
