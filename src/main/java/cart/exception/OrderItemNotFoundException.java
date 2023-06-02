package cart.exception;

public class OrderItemNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "주문에 포함된 아이템을 찾을 수 없습니다.";

    public OrderItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderItemNotFoundException(final String message) {
        super(message);
    }
}
