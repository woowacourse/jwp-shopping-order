package cart.exception;

public class ProductException extends RuntimeException {
    public ProductException(final String message) {
        super(message);
    }

    public static class IllegalId extends ProductException {
        public IllegalId(final Long id) {
            super("Illegal product item id; id=" + id);
        }
    }
}
