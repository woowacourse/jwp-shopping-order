package cart.exception;

public class DuplicateDiscountException extends RuntimeException {
    public DuplicateDiscountException(String message) {
        super(message);
    }
}
