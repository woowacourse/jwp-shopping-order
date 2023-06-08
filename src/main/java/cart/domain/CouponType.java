package cart.domain;

public class CouponType {

    private final long id;
    private final String name;
    private final DiscountType discountType;
    private final Money discountAmount;

    public CouponType(final long id, final String name, final DiscountType discountType, final Money discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }
}
