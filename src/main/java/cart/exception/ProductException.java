package cart.exception;

public class ProductException extends RuntimeException {

    public ProductException(final String message) {
        super(message);
    }

    public static class ProductNotExistException extends ProductException {

        public ProductNotExistException(final String message) {
            super(message);
        }
    }
}
