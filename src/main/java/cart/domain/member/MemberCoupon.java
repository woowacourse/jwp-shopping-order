package cart.domain.member;

import cart.domain.coupon.Coupon;

import java.util.Objects;

public class MemberCoupon {
    private final Long id;
    private final Coupon coupon;
    private final boolean used;

    public MemberCoupon(final Long id, final Coupon coupon, final boolean used) {
        this.id = id;
        this.coupon = coupon;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isUnUsed(){
        return !used;
    }

    public MemberCoupon use() {
        return new MemberCoupon(id, coupon, true);
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

    @Override
    public String toString() {
        return "MemberCoupon{" +
                "id=" + id +
                ", coupon=" + coupon +
                ", used=" + used +
                '}';
    }
}
