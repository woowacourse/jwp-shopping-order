package cart.exception;

public class NegativeCouponException extends RuntimeException {

    public NegativeCouponException(final String message) {
        super(message);
    }
}
