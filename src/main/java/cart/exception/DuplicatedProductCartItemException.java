package cart.exception;

public class DuplicatedProductCartItemException extends RuntimeException {
    public DuplicatedProductCartItemException(String message) {
        super(message);
    }
}
