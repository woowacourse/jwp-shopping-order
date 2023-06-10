package cart.exception;

public class CouponException extends RuntimeException {
    public CouponException() {
    }

    public CouponException(String message) {
        super(message);
    }

    public static class NoExist extends CouponException {
        public NoExist(String message) {
            super(message);
        }
    }

    public static class Unavailable extends CouponException {
        public Unavailable(final String message) {
            super(message);
        }
    }
}
