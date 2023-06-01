package cart.domain;

public class MemberCoupon {

    private final Long id;
    private final Coupon coupon;

    public MemberCoupon(final Long id, final Coupon coupon) {
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
