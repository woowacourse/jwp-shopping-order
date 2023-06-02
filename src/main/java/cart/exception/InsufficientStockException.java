package cart.exception;

public class InsufficientStockException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "남은 수량보다 많은 주문 요청입니다.";

    public InsufficientStockException() {
        super(DEFAULT_MESSAGE);
    }

    public InsufficientStockException(final String message) {
        super(message);
    }
}
