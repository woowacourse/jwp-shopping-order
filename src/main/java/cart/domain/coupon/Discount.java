package cart.domain.coupon;

public class Discount {

    private final DiscountType discountType;
    private final int amount;

    public Discount(final DiscountType discountType, final int amount) {
        this.discountType = discountType;
        this.amount = amount;
    }

    public Discount(final String discountType, final int amount) {
        this(DiscountType.valueOf(discountType), amount);
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public int getAmount() {
        return amount;
    }
}
