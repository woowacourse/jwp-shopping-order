package cart.exception;

public class CouponException extends RuntimeException {

    private final String message;

    public CouponException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
