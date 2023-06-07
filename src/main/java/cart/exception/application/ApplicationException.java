package cart.exception.application;

import cart.exception.ExpectedException;

public class ApplicationException extends ExpectedException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
