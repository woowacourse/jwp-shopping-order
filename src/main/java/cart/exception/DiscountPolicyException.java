package cart.exception;

public class DiscountPolicyException extends RuntimeException {
    public DiscountPolicyException(final String message) {
        super(message);
    }

    public static class InvalidPolicy extends DiscountPolicyException {
        public InvalidPolicy(final String message) {
            super(message);
        }
    }
}
