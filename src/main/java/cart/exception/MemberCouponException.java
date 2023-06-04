package cart.exception;

import cart.ui.advcie.ErrorType;

public class MemberCouponException extends RuntimeException {
    public MemberCouponException() {
    }

    public MemberCouponException(String message) {
        super(message);
    }

    public static class NoExist extends MemberCouponException {
        public NoExist(String message) {
            super(message);
        }
    }

    public static class Unavailable extends MemberCouponException {
        public Unavailable(final String message) {
            super(message);
        }
    }

    public static class IllegalMember extends MemberCouponException {
        public IllegalMember(final String message) {
            super(message);
        }
    }
}
