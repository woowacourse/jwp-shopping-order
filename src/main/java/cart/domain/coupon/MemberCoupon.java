package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.vo.Price;
import cart.exception.MemberCouponNotOwnerException;
import java.util.Objects;

public class MemberCoupon {

    private final Long id;
    private final Coupon coupon;
    private final Long memberId;

    public MemberCoupon(final Long id, final Coupon coupon, final Long memberId) {
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
    }

    public MemberCoupon(final Coupon coupon, final Long memberId) {
        this(null, coupon, memberId);
    }

    public void checkOwner(final Member member) {
        if (!member.getId().equals(memberId)) {
            throw new MemberCouponNotOwnerException();
        }
    }

    public Price discount(final Price originPrice) {
        return coupon.calculateDiscountPrice(originPrice);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberCoupon that = (MemberCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
