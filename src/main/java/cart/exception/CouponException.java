package cart.exception;

import cart.domain.Coupon;
import cart.domain.Member;

public class CouponException extends RuntimeException {

    public CouponException(final String message) {
        super(message);
    }

    public static class IllegalId extends CouponException {

        public IllegalId(final Long id) {
            super("Illegal couponId; couponId=" + id);
        }
    }


    public static class IllegalMember extends CouponException {
        public IllegalMember(final Coupon coupon, final Member member) {
            super("Illegal member attempts to coupon; couponId=" + coupon.getId() + ", memberId=" + member.getId());
        }
    }

    public static class AlreadyUsed extends CouponException {
        public AlreadyUsed(final Long id) {
            super("cannot use already used coupon; couponId=" + id);
        }
    }

    public static class AlreadyUsable extends CouponException {
        public AlreadyUsable(final Long id) {
            super("cannot refund already usable coupon; couponId=" + id);
        }
    }
}
