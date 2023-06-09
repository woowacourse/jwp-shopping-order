package cart.exception;

public class ProductException extends CustomException {
    public ProductException(final ErrorMessage message) {
        super(message);
    }
}
