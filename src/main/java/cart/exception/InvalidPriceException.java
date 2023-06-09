package cart.exception;

public class InvalidPriceException extends ShoppingOrderException {
    public InvalidPriceException(final String message) {
        super(message);
    }
}
