package cart.domain.coupon;

import cart.domain.Price;

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
}
