package cart.exception;

public class NoSuchProductException extends ShoppingOrderException {
    public NoSuchProductException(final String message) {
        super(message);
    }
}
