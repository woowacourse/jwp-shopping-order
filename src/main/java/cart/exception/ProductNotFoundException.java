package cart.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "상품을 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ProductNotFoundException(final String message) {
        super(message);
    }
}
