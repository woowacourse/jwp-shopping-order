package cart.exception;

public class IllegalOrderException extends ShoppingOrderException {
    public IllegalOrderException(String message) {
        super(message);
    }
}
