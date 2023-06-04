package cart.exception;

import cart.ui.advcie.ErrorType;

public class NoExpectedException extends RuntimeException {
    public NoExpectedException(final String message) {
        super(message);
    }
}
