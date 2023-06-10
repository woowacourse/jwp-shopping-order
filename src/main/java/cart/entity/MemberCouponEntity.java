package cart.entity;

import java.util.Objects;

public class MemberCouponEntity {

    private final Long id;
    private final CouponEntity coupon;
    private final boolean used;

    public MemberCouponEntity(final Long id, final CouponEntity coupon, final boolean used) {
        this.id = id;
        this.coupon = coupon;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCouponEntity that = (MemberCouponEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
