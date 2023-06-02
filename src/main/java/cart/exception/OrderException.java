package cart.exception;

public class OrderException extends RuntimeException {
    public OrderException(final String message) {
        super(message);
    }

    public static class EmptyOrder extends OrderException {
        public EmptyOrder() {
            super("최소 하나 이상의 상품이 포함되어야 합니다.");
        }
    }
}
