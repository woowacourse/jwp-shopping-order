package cart.exception;

public class ProductNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "product 이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    public ProductNotFoundException(final String message) {
        super(message);
    }
}
