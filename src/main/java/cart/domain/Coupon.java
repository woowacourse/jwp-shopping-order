package cart.domain;

import cart.exception.CouponException;

public class Coupon {

    private final Long id;
    private final CouponType couponType;
    private final boolean isUsed;

    public Coupon(final Long id, final CouponType couponType, final boolean isUsed) {
        this.id = id;
        this.couponType = couponType;
        this.isUsed = isUsed;
    }

    public Coupon(final Long id) {
        this(id, null, false);
    }

    public Money discount(final Money price) {
        if (couponType.getDiscountType() == DiscountType.DISCOUNT_PRICE) {
            return price.minus(couponType.getDiscountAmount());
        }
        throw new IllegalStateException("no discount type defined");
    }

    public Coupon use() {
        if (isUsed) {
            throw new CouponException.AlreadyUsed(id);
        }
        return new Coupon(id, couponType, true);
    }

    public Coupon refund() {
        if (!isUsed) {
            throw new CouponException.AlreadyUsable(id);
        }
        return new Coupon(id, couponType, false);
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
