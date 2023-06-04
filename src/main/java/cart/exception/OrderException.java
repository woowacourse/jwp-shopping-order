package cart.exception;

import cart.ui.advcie.ErrorType;

public class OrderException extends RuntimeException {
    public OrderException() {
    }

    public OrderException(String message) {
        super(message);
    }

    public static class NoExist extends OrderException {
        public NoExist(String message) {
            super(message);
        }
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(final String message) {
            super(message);
        }
    }
}
