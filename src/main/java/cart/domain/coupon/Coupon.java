package cart.domain.coupon;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
