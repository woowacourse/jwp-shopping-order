package cart.exception;

public class NotContainedCartItemException extends RuntimeException {

    public NotContainedCartItemException(String message) {
        super(message);
    }
}
