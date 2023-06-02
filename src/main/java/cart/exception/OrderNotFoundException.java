package cart.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "주문을 찾을 수 없습니다.";

    public OrderNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderNotFoundException(final String message) {
        super(message);
    }
}
