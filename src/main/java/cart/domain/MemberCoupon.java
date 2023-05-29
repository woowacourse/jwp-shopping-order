package cart.domain;

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

    public TotalPrice calculatePrice(final TotalPrice orderPrice) {
        return coupon.apply(orderPrice);
    }

    public TotalPrice calculateDiscountedPrice(final TotalPrice orderPrice) {
        return coupon.calculateDiscountedPrice(orderPrice);
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
        public MemberCoupon use() {
            return this;
        }

        @Override
        public boolean isUsed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Long getId() {
            throw new UnsupportedOperationException();
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
