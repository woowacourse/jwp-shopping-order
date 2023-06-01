package cart.domain.member;

import cart.domain.coupon.Coupon;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCoupon that = (MemberCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
