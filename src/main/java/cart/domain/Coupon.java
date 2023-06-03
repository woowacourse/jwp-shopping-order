package cart.domain;

import cart.dao.entity.CouponEntity;

public class Coupon {

    private final Long id;
    private final CouponType couponType;
    private final boolean isUsed;

    private Coupon(final Long id, final CouponType couponType, final boolean isUsed) {
        this.id = id;
        this.couponType = couponType;
        this.isUsed = isUsed;
    }

    public Coupon(final Long id) {
        this(id, null, false);
    }

    public static Coupon of(final CouponEntity coupon, final CouponType couponType) {
        return new Coupon(coupon.getId(),
                couponType,
                coupon.isUsed());
    }

    public Money discount(final Money price) {
        if (couponType.getDiscountType() == DiscountType.DISCOUNT_PRICE) {
            return price.minus(couponType.getDiscountAmount());
        }
        throw new IllegalStateException("no discount type defined");
    }

    public Long getId() {
        return id;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
