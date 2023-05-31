package cart.domain;

import java.util.Objects;

import cart.exception.InaccessibleCouponException;
import cart.exception.UsedCouponException;

public class MemberCoupon {

    private final Long id;
    private final Member owner;
    private final Coupon coupon;
    private boolean isUsed;

    public MemberCoupon(Member owner, Coupon coupon) {
        this(null, owner, coupon, false);
    }

    public MemberCoupon(Long id, Member owner, Coupon coupon) {
        this(id, owner, coupon, false);
    }

    public MemberCoupon(Long id, Member owner, Coupon coupon, boolean isUsed) {
        this.id = id;
        this.owner = owner;
        this.coupon = coupon;
        this.isUsed = isUsed;
    }

    public Money discount(Money money) {
        validateUnused();
        this.isUsed = true;
        return coupon.discount(money);
    }

    public void checkOwnerIs(Member member) {
        if (!owner.equals(member)) {
            throw new InaccessibleCouponException();
        }
    }

    private void validateUnused() {
        if (this.isUsed) {
            throw new UsedCouponException();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getOwner() {
        return owner;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MemberCoupon that = (MemberCoupon)o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
