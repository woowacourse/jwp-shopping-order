package cart.exception;

import cart.ui.advcie.ErrorType;

public class ProductException extends RuntimeException {
    public ProductException() {
    }

    public ProductException(String message) {
        super(message);
    }

    public static class NoExist extends ProductException {
        public NoExist(String message) {
            super(message);
        }
    }
}
