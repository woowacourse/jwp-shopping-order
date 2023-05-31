package cart.domain.coupon;

public class Coupon {

    private final Long id;
    private final String name;
    private final Discount discount;

    public Coupon(final Long id, final String name, final Discount discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public Coupon(final String name, final Discount discount) {
        this(null, name, discount);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Discount getDiscount() {
        return discount;
    }
}
