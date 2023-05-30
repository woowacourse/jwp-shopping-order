package cart.domain.member;

import cart.domain.TotalPrice;
import cart.domain.coupon.Coupon;
import cart.exception.CouponAlreadyUsedException;

public class MemberCoupon {

    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final boolean used;

    public MemberCoupon(final Member member, final Coupon coupon, final boolean used) {
        this(null, member, coupon, used);
    }

    public MemberCoupon(final Long id, final Member member, final Coupon coupon, final boolean used) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.used = used;
    }

    public MemberCoupon use() {
        if (used) {
            throw new CouponAlreadyUsedException();
        }
        return new MemberCoupon(id, member, coupon, true);
    }

    public long discountOrderPrice(final TotalPrice totalPrice) {
        return coupon.discountOrderPrice(totalPrice);
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

    public Member getMember() {
        return member;
    }

    public static final class NullMemberCoupon extends MemberCoupon {
        public NullMemberCoupon() {
            super(null, null, false);
        }

        @Override
        public long discountOrderPrice(final TotalPrice totalPrice) {
            return 0;
        }

        @Override
        public MemberCoupon use() {
            return this;
        }

        @Override
        public boolean isUsed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Long getId() {
            return null;
        }

        @Override
        public Coupon getCoupon() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Member getMember() {
            throw new UnsupportedOperationException();
        }
    }
}
