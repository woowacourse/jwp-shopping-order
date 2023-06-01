package cart.domain.coupon;

import cart.domain.vo.Price;
import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final Integer value;
    private final CouponType couponType;

    public Coupon(final Long id, final String name, final Integer value, final CouponType couponType) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.couponType = couponType;
    }

    public Price calculateDiscountPrice(final Price price) {
        if (couponType == CouponType.RATE_DISCOUNT) {
            return price.divide(value);
        }
        return new Price(value);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
