package cart.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(final String message) {
        super(message);
    }
}
