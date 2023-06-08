package cart.exception;

public class DiscountPolicyException extends RuntimeException {
    public DiscountPolicyException(final String message) {
        super(message);
    }

    public static class InvalidPolicyException extends DiscountPolicyException {
        public InvalidPolicyException(final String message) {
            super(message);
        }
    }
}
