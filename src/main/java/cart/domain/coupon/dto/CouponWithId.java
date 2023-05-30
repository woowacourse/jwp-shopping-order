package cart.domain.coupon.dto;

import cart.domain.coupon.Coupon;

public class CouponWithId {

    private final Long id;
    private final Coupon coupon;

    public CouponWithId(final Long id, final Coupon coupon) {
        this.id = id;
        this.coupon = coupon;
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
