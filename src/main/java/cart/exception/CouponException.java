package cart.exception;

public class CouponException extends RuntimeException {

    public CouponException(final String message) {
        super(message);
    }

    public static class IllegalId extends CouponException {

        public IllegalId(final Long id) {
            super("Illegal coupon id; id = " + id);
        }
    }
}
