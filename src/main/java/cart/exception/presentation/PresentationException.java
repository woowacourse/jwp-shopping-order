package cart.exception.presentation;

import cart.exception.ExpectedException;

public class PresentationException extends ExpectedException {

    public PresentationException(String message) {
        super(message);
    }

    public PresentationException(String message, Throwable cause) {
        super(message, cause);
    }
}
