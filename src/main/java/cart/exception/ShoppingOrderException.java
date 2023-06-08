package cart.exception;

public abstract class ShoppingOrderException extends RuntimeException {
    protected ShoppingOrderException(String message) {
        super(message);
    }
}
